package io.adrenaline.secrets.models;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class SecretModel {
    private static final String NAME = "name";
    private static final String TYPE = "type";
    protected static final String CONTENT = "content";

    public enum GroupType {
        PASSWORD,
        NOTE,
    }

    private String mName;

    public abstract GroupType getType();

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
        value.put(TYPE, getType().toString());
        value.put(CONTENT, getContentObject());
        return value;
    }

    protected static SecretModel create(JSONObject obj) throws JSONException {
        GroupType type = GroupType.valueOf(obj.getString(TYPE));
        if (type == GroupType.NOTE) {
            return new NoteSecretModel(obj);
        } else if (type == GroupType.PASSWORD) {
            return new PasswordSecretModel(obj);
        }
        return null;
    }

    public abstract String getLabels();
}

