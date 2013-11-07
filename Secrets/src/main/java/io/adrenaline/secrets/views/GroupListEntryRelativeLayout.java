package io.adrenaline.secrets.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.SecretGroupModel;
import io.adrenaline.secrets.models.Secrets;

/**
 * Created by stang6 on 11/1/13.
 */
public class GroupListEntryRelativeLayout extends RelativeLayout {
    private ImageView mGroupIcon;
    private TextView mGroupName;
    private TextView mGroupLabels;
    private ImageButton mShowGroupInfoButton;

    private SecretGroupModel mGroup;

    public interface Callbacks {
        /**
         * Callback for when an the entry has been selected.
         * @param index
         */
        public void onEntrySelected(int index);
        /**
         * Callback for when an the show info button is clicked.
         * @param index
         */
        public void onEntryInfoClicked(int index);
    }

    private Callbacks mCallbacks = sDummyCallbacks;

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onEntrySelected(int index) {
        }

        @Override
        public void onEntryInfoClicked(int index) {
        }
    };

    public GroupListEntryRelativeLayout(Context context) {
        super(context);
    }

    public GroupListEntryRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupListEntryRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mGroupIcon = (ImageView) findViewById(R.id.group_icon);
        mGroupName = (TextView) findViewById(R.id.group_title);
        mGroupLabels = (TextView) findViewById(R.id.group_labels);
        mShowGroupInfoButton = (ImageButton) findViewById(R.id.show_detail_fragment_button);
        mShowGroupInfoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGroup != null) {
                    mCallbacks.onEntryInfoClicked(Secrets.indexOfSecretGroup(mGroup));
                }
            }
        });
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGroup != null) {
                    mCallbacks.onEntrySelected(Secrets.indexOfSecretGroup(mGroup));
                }
            }
        });
    }

    public void update(SecretGroupModel group) {
        mGroup = group;
        // FIXME
        if (mGroup.getACL().size() > 1) {
            mGroupIcon.setImageResource(R.drawable.ic_action_group);
        } else {
            mGroupIcon.setImageResource(R.drawable.ic_action_person);
        }
        mGroupName.setText(group.getName());
        mGroupLabels.setText(group.getModifiedTime().toString());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Activity activity = (Activity) getContext();
        if (activity instanceof Callbacks) {
            mCallbacks = (Callbacks) activity;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mCallbacks = sDummyCallbacks;
    }
}
