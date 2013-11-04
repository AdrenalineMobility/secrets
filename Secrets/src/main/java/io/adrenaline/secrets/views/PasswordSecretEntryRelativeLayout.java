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

/**
 * Created by stang6 on 11/4/13.
 */
public class PasswordSecretEntryRelativeLayout extends RelativeLayout {
    private ImageView mGroupIcon;
    private TextView mGroupName;
    private TextView mGroupLabels;
    private ImageButton mShowGroupInfoButton;

    private PasswordSecretModel mPassword;

    public interface Callbacks {
        /**
         * Callback for when an the entry has been selected.
         * @param id
         */
        public void onEntrySelected(String id);
        /**
         * Callback for when an the show info button is clicked.
         * @param id
         */
        public void onEntryInfoClicked(String id);
    }

    private Callbacks mCallbacks = sDummyCallbacks;

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onEntrySelected(String id) {
        }

        @Override
        public void onEntryInfoClicked(String id) {
        }
    };

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

        mGroupIcon = (ImageView) findViewById(R.id.group_icon);
        mGroupName = (TextView) findViewById(R.id.group_title);
        mGroupLabels = (TextView) findViewById(R.id.group_labels);
        mShowGroupInfoButton = (ImageButton) findViewById(R.id.show_detail_fragment_button);
        mShowGroupInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword != null) {
                    //mCallbacks.onEntryInfoClicked(mGroup.getId());
                }
            }
        });
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword != null) {
                    // mCallbacks.onEntrySelected(mGroup.getId());
                }
            }
        });
    }

    public void update(PasswordSecretModel password) {
        mPassword = password;

        mGroupName.setText(mPassword.getName());
        mGroupLabels.setText(mPassword.getWebSite());
    }
}
