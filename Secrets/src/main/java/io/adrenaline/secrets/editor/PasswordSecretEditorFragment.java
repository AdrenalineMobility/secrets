package io.adrenaline.secrets.editor;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.NoteSecretModel;
import io.adrenaline.secrets.models.PasswordSecretModel;

/**
 * Created by stang6 on 11/11/13.
 */
public class PasswordSecretEditorFragment extends SecretEditorFragment {

    private PasswordSecretModel mPasswordSecret;
    private TextView mWebsite;
    private TextView mUsername;
    private TextView mPassword;

    public PasswordSecretEditorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPasswordSecret = (PasswordSecretModel) getSecret();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        setEditorContent(R.layout.editor_password_content);
        mWebsite = (TextView) root.findViewById(R.id.editor_password_website);
        mWebsite.setText(mPasswordSecret.getWebSite());
        mUsername = (TextView) root.findViewById(R.id.editor_password_username);
        mUsername.setText(mPasswordSecret.getUsername());
        mPassword = (TextView) root.findViewById(R.id.editor_password_password);
        mPassword.setText(mPasswordSecret.getPassword());

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    save();
                    getActivity().finish();
                    handled = true;
                }
                return handled;
            }
        });

        return root;
    }

    @Override
    protected void save() {
        mPasswordSecret.setWebsite(mWebsite.getText().toString());
        mPasswordSecret.setUsername(mUsername.getText().toString());
        mPasswordSecret.setPassword(mPassword.getText().toString());
        super.save();
    }
}
