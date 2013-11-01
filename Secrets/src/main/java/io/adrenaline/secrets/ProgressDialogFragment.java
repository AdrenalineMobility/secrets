package io.adrenaline.secrets;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ProgressDialogFragment extends DialogFragment {
    private static final String TAG = "ProgressDialogFragment";
    private TextView mText;
    private String mPendingText;

    public static ProgressDialogFragment showDialog(Activity activity) {
        FragmentManager fm = activity.getFragmentManager();
        ProgressDialogFragment dialog = new ProgressDialogFragment();
        if (fm == null) {
            Log.e(TAG, "Could not get fragment manager, not showing progress dialog");
        } else {
            dialog.show(fm, "fragment_progress_dialog");
        }
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_progress_dialog, container, false);
        if (rootView == null)
            return null;

        mText = (TextView) rootView.findViewById(R.id.progress_text);
        if (mText != null && mPendingText != null) {
            mText.setText(mPendingText);
            mPendingText = null;
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            int width = getActivity().getResources().getDisplayMetrics().widthPixels;
            int height = getActivity().getResources().getDisplayMetrics().heightPixels;
            if (getDialog() != null) {
                getDialog().getWindow().setLayout(width, height);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_Panel);
    }

    public void setText(String txt) {
        if (mText == null) {
            mPendingText = txt;
        } else {
            mText.setText(txt);
        }
    }
}