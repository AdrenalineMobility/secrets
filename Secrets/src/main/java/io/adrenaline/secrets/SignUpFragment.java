package io.adrenaline.secrets;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SignUpFragment extends Fragment {
    OnHaveAccountPressedListener mHaveAccountPressedListener;

    public interface OnHaveAccountPressedListener {
        public void onHaveAccountPressed();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mHaveAccountPressedListener = (OnHaveAccountPressedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHaveAccountPressedListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        if (rootView == null)
            return null;

        Button actionBtn = (Button) rootView.findViewById(R.id.create_account_log_in);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialogFragment dialog = ProgressDialogFragment.showDialog(SignUpFragment.this);
                dialog.setText("Creating account...");
            }
        });

        TextView bottomTxt = (TextView) rootView.findViewById(R.id.bottom_text);
        bottomTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHaveAccountPressedListener.onHaveAccountPressed();
            }
        });
        return rootView;
    }
}
