package io.adrenaline.secrets.models;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.views.GroupListEntryRelativeLayout;
import io.adrenaline.secrets.views.NoteSecretEntryRelativeLayout;
import io.adrenaline.secrets.views.PasswordSecretEntryRelativeLayout;

public class SecretGroupModel {
    private static final String NAME = "name";
    private static final String SECRETS = "secrets";
    private static final String LAST_MODIFIED_TIME = "last_modified_time";
    private static final String LAST_MODIFIER = "modifier";

    public static interface SecretGroupListener {
        public void onSecretGroupChanged();
    }

    private SecretGroupListener mListener;

    public void setListener(SecretGroupListener l) {
        mListener = l;
    }

    // test only
    protected static SecretGroupModel createRandomData() {
        SecretGroupModel model = new SecretGroupModel(new Random().nextInt() + "");
        model.setId(new Random().nextInt() + "");
        for (int i = 0; i < 5; ++i) {
            SecretModel.GroupType type = SecretModel.GroupType.values()[Math.abs(new Random().nextInt()) % 2];
            if (type == SecretModel.GroupType.NOTE) {
                byte[] content = new byte[100];
                new Random().nextBytes(content);
                for (int j = 0; j < content.length; ++j) {
                    content[j] = (byte) ('a' + content[j] % 26);
                }
                NoteSecretModel note = new NoteSecretModel("Note " + i, new String(content));
                model.addSecret(note);
            } else if (type == SecretModel.GroupType.PASSWORD) {
                byte[] content = new byte[100];
                new Random().nextBytes(content);
                PasswordSecretModel password = new PasswordSecretModel("Password " + i, "google.com", "username", "password");
                model.addSecret(password);
            }
        }

        int num = Math.abs(new Random().nextInt() % 5) + 1;
        for (int i = 0; i < num; ++i) {
            model.mAcl.add("user " + i);
        }

        return model;
    }

    private String mId;
    private String mName;
    private ArrayList<SecretModel> mSecrets = new ArrayList<SecretModel>();
    private Date mLastModified;
    private String mModifier;

    public SecretGroupModel(String name) {
        mName = name;
        mLastModified = new Date();
        mModifier = "me";
    }

    public SecretGroupModel(List<String> list) throws JSONException {
        String metaString = list.remove(0);
        JSONObject meta = new JSONObject(metaString);
        mName = meta.getString(NAME);
        mLastModified = new Date(meta.optLong(LAST_MODIFIED_TIME, new Date().getTime()));
        mModifier = meta.optString(LAST_MODIFIER, mModifier);

        for (String secret : list) {
            mSecrets.add(SecretModel.create(new JSONObject(secret)));
        }

        updated();
    }

    public SecretGroupModel(JSONObject object) throws JSONException {
        mName = object.getString(NAME);
        JSONArray secrets = object.getJSONArray(SECRETS);
        for (int i = 0; i < secrets.length(); ++i) {
            mSecrets.add(SecretModel.create(secrets.getJSONObject(i)));
        }
        mLastModified = new Date(object.optLong(LAST_MODIFIED_TIME, new Date().getTime()));
        mModifier = object.optString(LAST_MODIFIER, mModifier);
    }

    public void setId(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }


    // for test only
    private ArrayList<String> mAcl = new ArrayList<>();

    public List<String> getACL() {
        return mAcl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
        updated();
    }

    public SecretModel getSecret(int index) {
        return mSecrets.get(index);
    }

    public int indexOfSecret(SecretModel secret) {
        return mSecrets.indexOf(secret);
    }

    public int getNumOfSecrets() {
        return mSecrets.size();
    }

    public void removeSecret(SecretModel secret) {
        mSecrets.remove(secret);
        updated();
    }

    public void removeSecret(int index) {
        mSecrets.remove(index);
        updated();
    }

    public void updateSecret(SecretModel mSecret) {
        updated();
    }


    public boolean addSecret(SecretModel secret) {
        boolean result = mSecrets.add(secret);
        if (result) {
            updated();
        }
        return result;
    }

    protected void updated() {
        mLastModified = new Date();
        mModifier = "me";
        if (mListener != null) {
            mListener.onSecretGroupChanged();
        }
    }

    public Date getModifiedTime() {
        return mLastModified;
    }

    public String getModifier() {
        return mModifier;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(NAME, mName);
        JSONArray secrets = new JSONArray();
        for (SecretModel secret : mSecrets) {
            secrets.put(secret.toJSONObject());
        }
        object.put(SECRETS, secrets);
        object.put(LAST_MODIFIED_TIME, mLastModified.getTime());
        // FIXME: this should be User.getCurrentUser().getUserId()
        object.put(LAST_MODIFIER, mModifier);
        return object;
    }

    private SecretGroupAdapter mAdapter;

    public BaseAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new SecretGroupAdapter();
            SecretGroupModel.this.setListener(mAdapter);
        }
        return mAdapter;
    }

    private class SecretGroupAdapter extends BaseAdapter implements SecretGroupListener {

        @Override
        public int getCount() {
            return SecretGroupModel.this.getNumOfSecrets();
        }

        @Override
        public SecretModel getItem(int position) {
            return SecretGroupModel.this.getSecret(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            SecretModel.GroupType type = getItem(position).getType();
            return type.ordinal();
        }

        @Override
        public int getViewTypeCount() {
            return SecretModel.GroupType.values().length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SecretModel secret = getItem(position);
            if (secret.getType() == SecretModel.GroupType.NOTE) {
                NoteSecretEntryRelativeLayout noteEntry;
                if (convertView != null && convertView instanceof NoteSecretEntryRelativeLayout) {
                    noteEntry = (NoteSecretEntryRelativeLayout) convertView;
                } else {
                    LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
                    noteEntry = (NoteSecretEntryRelativeLayout) inflater.inflate(R.layout.note_secret_list_entry, parent, false);
                }

                noteEntry.update(secret);
                return noteEntry;
            } else if (secret.getType() == SecretModel.GroupType.PASSWORD) {
                PasswordSecretEntryRelativeLayout passwordEntry;
                if (convertView != null && convertView instanceof PasswordSecretEntryRelativeLayout) {
                    passwordEntry = (PasswordSecretEntryRelativeLayout) convertView;
                } else {
                    LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
                    passwordEntry = (PasswordSecretEntryRelativeLayout) inflater.inflate(R.layout.password_secret_list_entry, parent, false);
                }

                passwordEntry.update(secret);
                return passwordEntry;
            } else {
                return null;
            }
        }

        @Override
        public void onSecretGroupChanged() {
            notifyDataSetChanged();
        }
    }
}
