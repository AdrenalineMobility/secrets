package io.adrenaline.secrets.views;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.SecretModel;

/**
 * Created by stang6 on 11/5/13.
 */
public abstract class SecretEntryRelativeLayout extends RelativeLayout {
    private SecretModel mSecret;
    private ImageView mIcon;
    private TextView mName;
    private TextView mLabels;
    private ImageButton mEdit;
    private View mDetails;
    private boolean mShowingDetails = false;

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
        mEdit = (ImageButton) findViewById(R.id.edit_secret);
        mDetails = findViewById(R.id.secret_details);

        mEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onEditSecretClicked(mSecret);
            }
        });

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(!mShowingDetails);
            }
        });
    }

    protected ViewGroup addDetailView(int resId) {
        return (ViewGroup) inflate(this.getContext(), resId, (ViewGroup) mDetails);
    }

    public void update(SecretModel secret) {
        mSecret = secret;

        if (mSecret.getType() == SecretModel.GroupType.NOTE) {
            mIcon.setImageResource(R.drawable.ic_note);
        } else if (mSecret.getType() == SecretModel.GroupType.PASSWORD) {
            mIcon.setImageResource(R.drawable.ic_password);
        } else {
            return;
        }

        mName.setText(secret.getName());
        mLabels.setText(secret.getLabels());
    }

    public void toggle(boolean show) {
        mShowingDetails = show;
        if (show) {
            mDetails.setVisibility(View.VISIBLE);
            mName.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            mLabels.setVisibility(View.GONE);
        } else {
            mDetails.setVisibility(View.GONE);
            mName.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            mLabels.setVisibility(View.VISIBLE);
        }
    }

    public interface Callbacks {
        /**
         * Callback for when an the edit button is clicked.
         * @param secret
         */
        public void onEditSecretClicked(SecretModel secret);
    }

    private Callbacks mCallbacks = sDummyCallbacks;

    private static Callbacks sDummyCallbacks = new Callbacks() {

        @Override
        public void onEditSecretClicked(SecretModel secret) {
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Activity activity = (Activity) getContext();
        Fragment fragment = activity.getFragmentManager().findFragmentById(R.id.group_detail_container);
        if (fragment instanceof Callbacks) {
            mCallbacks = (Callbacks) fragment;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mCallbacks = sDummyCallbacks;
    }
}
