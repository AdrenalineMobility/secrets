package io.adrenaline.secrets.models;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordSecretModel extends SecretModel {
    private static final String WEBSITE = "website";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private String mWebsite = "";
    private String mUsername = "";
    private String mPassword = "";

    public PasswordSecretModel(String name, String website, String username, String password) {
        super(name);

        mWebsite = website;
        mUsername = username;
        mPassword = password;
    }

    protected PasswordSecretModel(JSONObject obj) {
        super(obj);

        JSONObject content = obj.optJSONObject(CONTENT);
        if (content != null) {
            mWebsite = content.optString(WEBSITE, "");
            mUsername = content.optString(USERNAME, "");
            mPassword = content.optString(PASSWORD, "");
        }
    }

    @Override
    protected JSONObject getContentObject() throws JSONException {
        return new JSONObject().put(WEBSITE, mWebsite).put(USERNAME, mUsername).put(mPassword, mPassword);
    }

    @Override
    public String getLabels() {
        return getUsername() + "@" + getWebSite();
    }

    @Override
    public GroupType getType() {
        return GroupType.PASSWORD;
    }

    public String getWebSite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public String getUsername() {
        return mUsername;
    }

    public  void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

}
