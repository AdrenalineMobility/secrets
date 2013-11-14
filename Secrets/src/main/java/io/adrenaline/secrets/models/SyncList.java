package io.adrenaline.secrets.models;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SyncList {
    private static final String LIST_KEY = "__LIST__";
    private static final String TAG = "SyncList";

    public static interface SyncListCallback {
        public abstract void done(List<String> list);
        public abstract void fail(String error);
    }

    /**
     * Invoke done handler in UI thread.
     */
    private static void postDone(final List<String> value, final SyncListCallback cb) {
        if (cb == null) return;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                cb.done(value);
            }
        });
    }

    /**
     * Invoke fail handler in UI thread.
     */
    private static void postFail(final String error, final SyncListCallback cb) {
        if (cb == null) return;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                cb.fail(error);
            }
        });
    }

    private String mName;
    private SyncContainer mContainer;

    public SyncList(String name) {
        mName = name;
        mContainer = null;
    }

    /**
     * This must be called from a thread, it might block
     *
     * This function will post a fail event iff this call fails (i.e., returns null).  Otherwise, it
     * does not invoke anything on the deferred object.
     */
    private boolean init(final SyncListCallback cb) {
        if (mContainer != null) return true;

        mContainer = SyncContainer.openNamedSyncContainer(mName);
        if (mContainer == null) {
            String lastError = SyncContainer.lastError();
            postFail(lastError, cb);
            return false;
        }
        return true;
    }

    /**
     * Fetch the map from the container and copy it to a HashMap
     *
     * This call might block and should be called from a thread
     */
    private synchronized void doGet(SyncListCallback cb) {
        try {
            if (!init(cb)) return;

            JSONArray array = mContainer.getJSONArray(LIST_KEY);
            ArrayList<String> list = new ArrayList<>();
            if (array != null) {
                for (int idx = 0; idx < array.length(); idx++) {
                    String value = array.getString(idx);
                    list.add(value);
                }
            }

            postDone(list, cb);
        } catch (Exception e) {
            Log.e(TAG, "unexpected exception", e);
            postFail("error_unexpected_exception", cb);
        }
    }



    public void get(final SyncListCallback cb) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                doGet(cb);
            }
        }).start();
    }

    private synchronized void doAdd(int idx, String element, SyncListCallback cb) {
        try {
            if (!init(cb)) return;

            if (!mContainer.add(LIST_KEY, idx, element)) {
                postFail(SyncContainer.lastError(), cb);
                return;
            }

            doGet(cb);
        } catch (Exception e) {
            Log.e(TAG, "unexpected exception", e);
            postFail("error_unexpected_exception", cb);
        }

    }

    public void add(final String element, final SyncListCallback cb) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                doAdd(-1, element, cb);
            }
        }).start();
    }

    /*
    public void add(int idx, String element, SyncListCallback cb) {

    }

    public void set(int idx, String element, SyncListCallback cb) {

    }

    public void remove(int idx, SyncListCallback cb) {

    }
    */
}
