package io.adrenaline.secrets.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.PasswordSecretModel;
import io.adrenaline.secrets.models.SecretModel;

/**
 * Created by stang6 on 11/4/13.
 */
public class PasswordSecretEntryRelativeLayout extends SecretEntryRelativeLayout {

    private PasswordSecretModel mPassword;
    private TextView mPasswordField;
    private TextView mUsernameField;

    public PasswordSecretEntryRelativeLayout(Context context) {
        super(context);
    }

    public PasswordSecretEntryRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PasswordSecretEntryRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View container = addDetailView(R.layout.password_secret_detail);
        mPasswordField = (TextView) container.findViewById(R.id.password_field);
        mUsernameField = (TextView) container.findViewById(R.id.username_field);
    }

    @Override
    public void update(SecretModel password) {
        super.update(password);
        mPassword = (PasswordSecretModel) password;

        mUsernameField.setText(mPassword.getUsername());
        mPasswordField.setText(mPassword.getPassword());
    }
}
