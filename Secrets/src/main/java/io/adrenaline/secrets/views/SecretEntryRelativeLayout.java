package io.adrenaline.secrets.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.NoteSecretModel;
import io.adrenaline.secrets.models.SecretGroupModel;
import io.adrenaline.secrets.models.SecretModel;

/**
 * Created by stang6 on 11/5/13.
 */
public class SecretEntryRelativeLayout extends RelativeLayout {
    private SecretModel mSecret;
    private ImageView mIcon;
    private TextView mName;
    private TextView mLabels;
    private ImageButton mToggle;

    public SecretEntryRelativeLayout(Context context) {
        super(context);
    }

    public SecretEntryRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SecretEntryRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mIcon = (ImageView) findViewById(R.id.secret_icon);
        mName = (TextView) findViewById(R.id.secret_name);
        mLabels = (TextView) findViewById(R.id.secret_labels);
        mToggle = (ImageButton) findViewById(R.id.toggle_secret);
    }

    public void update(SecretModel secret) {
        mSecret = secret;

        if (mSecret.getType() == SecretGroupModel.GroupType.NOTE) {
            mIcon.setImageResource(R.drawable.ic_note);
        } else if (mSecret.getType() == SecretGroupModel.GroupType.PASSWORD) {
            mIcon.setImageResource(R.drawable.ic_password);
        } else {
            return;
        }

        mName.setText(secret.getName());
        mLabels.setText(secret.getLabels());
    }
}
