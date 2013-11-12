package io.adrenaline.secrets.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.adrenaline.secrets.R;

/**
 * Created by stang6 on 11/11/13.
 */
public class ACLEntryRelativeLayout extends RelativeLayout {
    private TextView mUserId;
    private TextView mRole;
    private QuickContactBadge mBadge;

    public ACLEntryRelativeLayout(Context context) {
        super(context);
    }

    public ACLEntryRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ACLEntryRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mBadge = (QuickContactBadge) this.findViewById(R.id.share_badge);
        mUserId = (TextView) this.findViewById(R.id.share_name);
        mRole = (TextView) this.findViewById(R.id.share_role);
    }

    public void update(String id, boolean owner) {
        mBadge.setImageResource(R.drawable.ic_contact_list_picture);
        mUserId.setText(id);
        if (owner) {
            mRole.setText(R.string.owner);
        } else {
            mRole.setText(R.string.can_view);
        }
    }
}
