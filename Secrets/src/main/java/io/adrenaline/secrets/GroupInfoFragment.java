package io.adrenaline.secrets;


import android.os.Bundle;
import android.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.adrenaline.secrets.models.SecretGroupModel;
import io.adrenaline.secrets.models.Secrets;

public class GroupInfoFragment extends Fragment {
    public static final String ARG_CONTAINER_ID = "container_id";

    private String mLastModifiedFormat;
    private String mLastModifiedByMeFormat;
    private String mLastOpenedByMeFormat;

    private TextView mGroupTitle;
    private View mOfflineSwitch;
    private LinearLayout mShareList;
    private TextView mShareListWarning;
    private ProgressBar mShareListProgress;
    private TextView mLastModified;
    private TextView mLastModifiedByMe;
    private TextView mLastOpenedByMe;

    private ActionMode mActionMode;
    private SecretGroupModel mSecretGroup;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLastModifiedFormat = getResources().getString(R.string.fragment_group_info_general_info_modified);
        mLastModifiedByMeFormat = getResources().getString(R.string.fragment_group_info_general_info_modified_by_me);
        mLastOpenedByMeFormat = getResources().getString(R.string.fragment_group_info_general_info_opened_by_me);

        if (getArguments().containsKey(GroupDetailFragment.ARG_GROUP_INDEX)) {
            // Load the group content specified by the fragment
            mSecretGroup = Secrets.getSecretGroup(getArguments().getInt(GroupDetailFragment.ARG_GROUP_INDEX));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group_info, container, false);

        /* find views */
        mGroupTitle = (TextView) rootView.findViewById(R.id.title);
        mOfflineSwitch = rootView.findViewById(R.id.pin_compoundButton);
        mShareList = (LinearLayout) rootView.findViewById(R.id.share_list);
        mShareListWarning = (TextView) rootView.findViewById(R.id.share_list_warning);
        mShareListProgress = (ProgressBar) rootView.findViewById(R.id.share_list_progress_bar);
        mLastModified = (TextView) rootView.findViewById(R.id.last_modified);
        mLastModifiedByMe = (TextView) rootView.findViewById(R.id.last_modified_by_me);
        mLastOpenedByMe = (TextView) rootView.findViewById(R.id.last_opened_by_me);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mActionMode != null) {
            return;
        }
        // Start the CAB using the ActionMode.Callback defined below
        mActionMode = getActivity().startActionMode(mActionModeCallback);
        mGroupTitle.setText(mSecretGroup.getName());
        updateGeneralInfoPanel();
    }

    private void updateGeneralInfoPanel() {
        String modifiedTime = mSecretGroup.getModifiedTime().toString();
        String modifier = mSecretGroup.getModifier();
        String modifiedByMeTime = "";
        String openedByMeTime = "";
        mLastModified.setText(String.format(mLastModifiedFormat, modifiedTime, modifier));
        mLastModifiedByMe.setText(String.format(mLastModifiedByMeFormat, modifiedByMeTime));
        mLastOpenedByMe.setText(String.format(mLastOpenedByMeFormat, openedByMeTime));
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.group_info, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_group_rename:
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;

            getActivity().getFragmentManager().beginTransaction().remove(GroupInfoFragment.this).commit();
        }
    };
}