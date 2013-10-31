package io.adrenaline.secrets;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GroupInfoFragment extends Fragment {
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

    @Override
    public void onCreate (Bundle savedInstanceState) {
        mLastModifiedFormat = getResources().getString(R.string.fragment_group_info_general_info_modified);
        mLastModifiedByMeFormat = getResources().getString(R.string.fragment_group_info_general_info_modified_by_me);
        mLastOpenedByMeFormat = getResources().getString(R.string.fragment_group_info_general_info_opened_by_me);
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

    private void updateGeneralInfoPanel() {
        String modifiedTime = "";
        String modifier = "";
        String modifiedByMeTime = "";
        String openedByMeTime = "";
        mLastModified.setText(String.format(mLastModifiedFormat, modifiedTime, modifier));
        mLastModifiedByMe.setText(String.format(mLastModifiedByMeFormat, modifiedByMeTime));
        mLastOpenedByMe.setText(String.format(mLastOpenedByMeFormat, openedByMeTime));
    }
}