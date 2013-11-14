package io.adrenaline.secrets.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.adrenaline.ApiResponse;
import io.adrenaline.Storage;
import io.adrenaline.User;

public class SyncContainer {

    private static final int NUM_CAS_RETRIES = 5;
    private static ThreadLocal<String> mErrorString = new ThreadLocal<String>();

    /**
     * This class is something that should be merged with the main container class
     * but I have it separate for now while we get used to the APIs.  We must ensure that there
     * is only one SyncContainer for each Storage.Container in the system.
     *
     * These functions all block and should be called from threads.
     */
    private static final String TAG = "SyncContainer";
    private static HashMap<String, SyncContainer> sContainerMap = new HashMap<String, SyncContainer>();

    private Storage.Container mContainer;
    private boolean mHasFetched;

    /**
     * Get a pointer to the container specific SyncContainer.  This function will return a pointer
     * to the same object on subsequent calls if the caller passes in the same container.
     */
    public static synchronized SyncContainer getSyncContainer(Storage.Container container) {
        String containerId = container.getId();
        if (!sContainerMap.containsKey(containerId)) {
            SyncContainer scont = new SyncContainer(container);
            sContainerMap.put(containerId, scont);
        }
        return sContainerMap.get(containerId);
    }

    /**
     * Clears cached data, should be called at the same time that the Storage cache is cleared.
     */
    public static synchronized void clearSyncContainers() {
        sContainerMap.clear();
    }

    private static SyncContainer createSyncContainer(SyncContainer userContainer, String name) {
        mErrorString.set(null);
        Storage.Container container = Storage.createContainer();
        ApiResponse response = container.save();
        if (!response.ok()) {
            mErrorString.set(response.status());
            return null;
        }

        boolean added = userContainer.add(name, container.getId());
        if (!added) {
            return null;
        }

        return getSyncContainer(container);
    }

    /**
     * Looks up and returns named sync containers, creates new ones if needed.  Named SyncContainers
     * are SyncContainers that the caller can access using a user-specific name.
     *
     * This call will access the network and block, it should be called from a thread
     */
    public static SyncContainer openNamedSyncContainer(String name) {
        SyncContainer userContainer = getSyncContainer(User.getCurrentUser().getContainer());
        boolean gotData = userContainer.doFetch(false);
        if (!gotData) {return null;}

        String containerId = userContainer.mContainer.getString(name);
        if (containerId == null || containerId.length() == 0) {
            return createSyncContainer(userContainer, name);
        } else {
            return getSyncContainer(Storage.getContainer(containerId));
        }
    }

    private SyncContainer(Storage.Container container) {
        mContainer = container;
        mHasFetched = false;
    }

    public synchronized JSONObject getJSONObject(String name) {
        if (!doFetch(false)) return null;
        return mContainer.get(name);
    }

    public synchronized JSONArray getJSONArray(String name) {
        if (!doFetch(false)) return null;
        return mContainer.getJSONArray(name);
    }

    /**
     * Add a name/item pair to a map stored as the value of a container-level key/value pair.
     * The end result of this operation will be:
     *
     * container = {"key": {"name": "item"}}
     *
     * @return true if added, false if there was an error and the error string was set
     */
    public synchronized boolean add(final String key, final String name, final String item) {
        return doAdd(new GetAndSet() {
            @Override
            public boolean getAndSet() throws JSONException {
                JSONObject currValue = mContainer.get(key);
                if (currValue == null) {
                    currValue = new JSONObject();
                }
                if (currValue.has(name)) {
                    mErrorString.set("error_name_already_in_map");
                    return false;
                }

                currValue.put(name, item);
                mContainer.set(key, currValue);

                return true;
            }
        });
    }

    /**
     * Adds an element to a list stored at key.  The end result of this operation will be:
     *
     * container = {"key": ["element"]}
     *
     * @param idx the index where to insert the element.  If idx is less than 0 or >= length, then
     *            it will append the element.
     */
    public synchronized boolean add(final String key, final int idx, final String element) {
        return doAdd(new GetAndSet() {
            @Override
            public boolean getAndSet() throws JSONException {
                JSONArray array = mContainer.getJSONArray(key);
                if (array == null) {
                    array = new JSONArray();
                }
                JSONArray newArray;

                // sorry that this is kind of nasty, but JSONArray doesn't have an insert/add
                // method!!!
                if (idx < 0 || idx >= array.length()) {
                    array.put(element);
                    newArray = array;
                } else {
                    newArray = new JSONArray();
                    for (int i = 0; i < array.length(); i++) {
                        if (i == idx) {
                            newArray.put(element);
                            i--;
                        } else {
                            newArray.put(array.getString(i));
                        }
                    }
                }

                mContainer.set(key, newArray);
                return true;
            }
        });
    }

    /**
     * Add a key/value pair directly to the container.  The end result will be:
     *
     * container = {"key": "item"}
     *
     * @return true if added, false if there was an error and the error string was set
     */
    public synchronized boolean add(final String key, final String item) {
        return doAdd(new GetAndSet() {
            @Override
            public boolean getAndSet() throws JSONException {
                String currValue = mContainer.getString(key);
                if (currValue != null && currValue.length() > 0) {
                    mErrorString.set("error_name_already_in_map");
                    return false;
                }

                mContainer.set(key, item);

                return true;
            }
        });
    }

    /**
     * Uses thread-local storage to keep track of any error strings from get or add calls.  Note:
     * you must call this on the same thread as the get or add call were made on.
     *
     * @return error string, null if there wasn't an error
     */
    public static String lastError() {
        return mErrorString.get();
    }

    private synchronized boolean doFetch(boolean forceFetch) {
        mErrorString.set(null);
        if (!mHasFetched || forceFetch) {
            ApiResponse response = mContainer.fetch();
            if (!response.ok()) {
                mErrorString.set(response.status());
                return false;
            }
        }

        mHasFetched = true;
        return true;
    }

    private abstract class GetAndSet {
        public abstract boolean getAndSet() throws JSONException;
    }

    /**
     * Add the item to the container iff it does not already exist.
     * The caller uses GetAndSet classes to define how to get and set
     * the particular part of the container that it wants to manipulate, and
     * this function will take care of CAS and re-fetching data, if needed.
     *
     * Make sure you hold the lock when calling this.
     *
     */
    private boolean doAdd(GetAndSet gas) {
        mErrorString.set(null);
        try {
            boolean fetched = doFetch(false);
            if (!fetched) return false;

            for (int idx = 0; idx < NUM_CAS_RETRIES; idx++) {
                if (!gas.getAndSet()) return false;

                ApiResponse response = mContainer.save(true);
                if (response.ok()) {
                    return true;
                } else if (!response.status().equals("error_timestamp_mismatch")) {
                    // an error other than CAS conflict
                    mErrorString.set(response.status());
                    return false;
                }

                Log.d(TAG, "CONFLICT, trying again");

                // CAS conflict, re-fetch the container and try again
                fetched = doFetch(true);
                if (!fetched) return false;
            }
            mErrorString.set("error_too_many_cas_retries");
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            mErrorString.set("error_json_error");
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            mErrorString.set("error_unknown_error");
            return false;
        }
    }
}