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
         */
        public void onEntrySelected(SecretGroupModel group);
        /**
         * Callback for when an the show info button is clicked.
         */
        public void onEntryInfoClicked(SecretGroupModel group);
    }

    private Callbacks mCallbacks = sDummyCallbacks;

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onEntrySelected(SecretGroupModel group) {
        }

        @Override
        public void onEntryInfoClicked(SecretGroupModel group) {
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
                    mCallbacks.onEntryInfoClicked(mGroup);
                }
            }
        });
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGroup != null) {
                    mCallbacks.onEntrySelected(mGroup);
                }
            }
        });
    }

    public void update(SecretGroupModel group) {
        mGroup = group;
        // FIXME
        if (group.getType() == SecretGroupModel.GroupType.NOTE) {
            mGroupIcon.setImageResource(R.drawable.ic_launcher);
        } else {
            mGroupIcon.setImageResource(R.drawable.ic_launcher);
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
