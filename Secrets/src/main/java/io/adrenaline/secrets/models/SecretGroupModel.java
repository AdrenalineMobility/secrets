package io.adrenaline.secrets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class SecretGroupModel {
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String SECRETS = "secrets";
    private static final String LAST_MODIFIED_TIME = "last_modified_time";
    private static final String LAST_MODIFIER = "modifier";

    // test only
    protected static SecretGroupModel create() {
        SecretGroupModel model = new SecretGroupModel(GroupType.values()[Math.abs(new Random().nextInt()) % 2], new Random().nextInt() + "");
        return model;
    }

    public enum GroupType {
        PASSWORD,
        NOTE,
    }

    private GroupType mType;
    private String mName;
    private ArrayList<SecretModel> mSecrets = new ArrayList<SecretModel>();
    private Date mLastModified;
    private String mModifier;

    public SecretGroupModel(GroupType type, String name) {
        mType = type;
        mName = name;
        mLastModified = new Date();
        mModifier = "me";
    }

    public SecretGroupModel(JSONObject object) throws JSONException {
        mType = GroupType.valueOf(object.getString(TYPE));
        mName = object.getString(NAME);
        JSONArray secrets = object.getJSONArray(SECRETS);
        for (int i = 0; i < secrets.length(); ++i) {
            mSecrets.add(SecretModel.create(mType, secrets.getJSONObject(i)));
        }
        mLastModified = new Date(object.optLong(LAST_MODIFIED_TIME, new Date().getTime()));
        mModifier = object.optString(LAST_MODIFIER, mModifier);
    }

    public Object getACL() {
        return null;
    }

    public GroupType getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public SecretModel getSecret(int index) {
        return mSecrets.get(index);
    }

    public int getNumOfSecrets() {
        return mSecrets.size();
    }

    public void removeSecret(SecretModel secret) {
        mSecrets.remove(secret);
    }

    public void removeSecret(int index) {
        mSecrets.remove(index);
    }

    public boolean addSecret(SecretModel secret) {
        if (secret.getType() == this.getType()) {
            return mSecrets.add(secret);
        }
        return false;
    }

    public Date getModifiedTime() {
        return mLastModified;
    }

    public String getModifier() {
        return mModifier;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(TYPE, mType.toString());
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
}
