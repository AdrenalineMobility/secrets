package io.adrenaline.secrets.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.PasswordSecretModel;
import io.adrenaline.secrets.models.SecretModel;

/**
 * Created by stang6 on 11/4/13.
 */
public class PasswordSecretEntryRelativeLayout extends SecretEntryRelativeLayout {

    private PasswordSecretModel mPassword;

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

    }

    @Override
    public void update(SecretModel password) {
        super.update(password);
        mPassword = (PasswordSecretModel) password;
    }
}
