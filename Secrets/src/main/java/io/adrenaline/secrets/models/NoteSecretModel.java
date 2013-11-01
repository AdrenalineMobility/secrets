package io.adrenaline.secrets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stang6 on 11/1/13.
 */
public class NoteSecretModel extends SecretModel {
    private static final String NOTE = "note";
    private String mNote = "";

    public NoteSecretModel(String name, String note) {
        super(name);

        mNote = note;
    }

    protected NoteSecretModel(JSONObject obj) {
        super(obj);

        JSONObject content = obj.optJSONObject(CONTENT);
        if (content != null) {
            mNote = content.optString(NOTE, "");
        }
    }

    @Override
    protected JSONObject getContentObject() throws JSONException {
        return new JSONObject().put(NOTE, mNote);
    }

    @Override
    public SecretGroupModel.GroupType getType() {
        return SecretGroupModel.GroupType.NOTE;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }
}
