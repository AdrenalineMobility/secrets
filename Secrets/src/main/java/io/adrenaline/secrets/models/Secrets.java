package io.adrenaline.secrets.models;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.sync.SyncList;
import io.adrenaline.secrets.views.GroupListEntryRelativeLayout;

/**
 * Created by stang6 on 11/4/13.
 */
public class Secrets {
    private static final String TAG = "Secrets";
    private static final String SECRET_GROUPS = "secret_groups";

    private Secrets() {

    }

    /*package*/ static void generateRandomData() {
        for (int i = 0; i < 5; ++i) {
            sSecrets.add(SecretGroupModel.createRandomData());
        }
    }

    public static interface SecretsListener {
        public void onSecretsChanged();
    }

    private static ArrayList<SecretGroupModel> sSecrets = new ArrayList<SecretGroupModel>();

    private static SecretsListener sListener;

    public static void setListener(SecretsListener l) {
        sListener = l;
    }

    public static void sync() {
        sSecrets.clear();

        SyncList groupList = new SyncList(SECRET_GROUPS);
        groupList.get(new SyncList.SyncListCallback() {
            @Override
            public void done(List<String> list) {
                for (String groupId : list) {
                    syncGroup(groupId);
                }
            }

            @Override
            public void fail(String error) {
                // TODO
                Log.w(TAG, "Get secret groups: " + error);
            }
        });
    }

    private static void syncGroup(final String groupId) {
        SyncList group = new SyncList(groupId);
        group.get(new SyncList.SyncListCallback() {
            @Override
            public void done(List<String> list) {
                try {
                    updateSecretGroup(new SecretGroupModel(list));
                    Log.d(TAG, "Sync group " + groupId);
                } catch (JSONException e) {
                    Log.w(TAG, "Sync group " + groupId + ": " + e.getMessage());
                }
            }

            @Override
            public void fail(String error) {
                // TODO
                Log.w(TAG, "Sync group " + groupId + ": " + error);
            }
        });
    }

    public static void addSecretGroup(final SecretGroupModel group) throws JSONException {
        final String groupId = UUID.randomUUID().toString();
        SyncList syncGroup = new SyncList(groupId);
        syncGroup.add(group.getMetadata(), new SyncList.SyncListCallback() {
            @Override
            public void done(List<String> list) {
                addToGroupList(groupId, group);
            }

            @Override
            public void fail(String error) {
                // TODO
                Log.w(TAG, "Create group: " + error);
            }
        });
    }

    private static void addToGroupList(String groupId, final SecretGroupModel group) {
        SyncList groupList = new SyncList(SECRET_GROUPS);
        groupList.add(groupId, new SyncList.SyncListCallback() {
            @Override
            public void done(List<String> list) {
                updateSecretGroup(group);
                Log.d(TAG, "Add to group list");
            }

            @Override
            public void fail(String error) {
                // TODO
                Log.w(TAG, "Add to group list: " + error);
            }
        });
    }

    public static void updateSecretGroup(SecretGroupModel group) {
        if (indexOfSecretGroup(group) < 0) {
            sSecrets.add(group);
        }
        updated();
    }

    public static void removeSecretGroup(SecretGroupModel group) {
        if (sSecrets.remove(group)) {
            updated();
        }
    }

    private static void updated() {
        if (sListener != null) {
            sListener.onSecretsChanged();
        }
    }


    public static SecretGroupModel getSecretGroup(int index) {
        if (index < sSecrets.size()) {
            return sSecrets.get(index);
        }
        return null;
    }

    public static int indexOfSecretGroup(SecretGroupModel group) {
        return sSecrets.indexOf(group);
    }

    public static int numOfGroups() {
        return sSecrets.size();
    }

    private static SecretsAdapter sAdapter;
    public static BaseAdapter getAdapter() {
        if (sAdapter == null) {
            sAdapter = new SecretsAdapter();
            Secrets.setListener(sAdapter);
        }

        return sAdapter;
    }

    private static class SecretsAdapter extends BaseAdapter implements Secrets.SecretsListener {

        private SecretsAdapter() {
            if (Secrets.numOfGroups() == 0) {
                // Secrets.generateRandomData();
            }
        }

        @Override
        public int getCount() {
            return Secrets.numOfGroups();
        }

        @Override
        public SecretGroupModel getItem(int position) {
            return Secrets.getSecretGroup(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GroupListEntryRelativeLayout groupEntry;
            if (convertView != null && convertView instanceof GroupListEntryRelativeLayout) {
                groupEntry = (GroupListEntryRelativeLayout) convertView;
            } else {
                LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
                groupEntry = (GroupListEntryRelativeLayout) inflater.inflate(R.layout.group_list_entry, parent, false);
            }

            groupEntry.update(getItem(position));
            return groupEntry;
        }

        @Override
        public void onSecretsChanged() {
            notifyDataSetChanged();
        }
    }
}
