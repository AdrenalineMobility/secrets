package io.adrenaline.secrets.models;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class SecretModel {
    private static final String NAME = "name";
    protected static final String CONTENT = "content";

    private String mName;

    public abstract SecretGroupModel.GroupType getType();

    protected SecretModel(String name) {
        mName = name;
    }

    protected SecretModel(JSONObject obj) {
        mName = obj.optString(NAME, "");
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    abstract protected JSONObject getContentObject() throws JSONException;

    protected JSONObject toJSONObject() throws JSONException {
        JSONObject value = new JSONObject();
        value.put(NAME, mName);
        value.put(CONTENT, getContentObject());
        return value;
    }

    protected static SecretModel create(SecretGroupModel.GroupType mType, JSONObject obj) {
        if (mType == SecretGroupModel.GroupType.NOTE) {
            return new NoteSecretModel(obj);
        } else if (mType == SecretGroupModel.GroupType.PASSWORD) {
            return new PasswordSecretModel(obj);
        }
        return null;
    }

    public abstract String getLabels();
}

