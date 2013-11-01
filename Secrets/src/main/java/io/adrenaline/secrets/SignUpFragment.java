package io.adrenaline.secrets;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpFragment extends Fragment {
    private OnSignUpListener mSignUpListener;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mVerifyPassword;

    public interface OnSignUpListener {
        public void onHaveAccountPressed();
        public void onSignUp(String username, String password);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mSignUpListener = (OnSignUpListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHaveAccountPressedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        if (rootView == null)
            return null;

        mUsername = (EditText) rootView.findViewById(R.id.username);
        mPassword = (EditText) rootView.findViewById(R.id.password);
        mVerifyPassword = (EditText) rootView.findViewById(R.id.verify_password);

        Button actionBtn = (Button) rootView.findViewById(R.id.create_account_log_in);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUsername == null || mPassword == null || mVerifyPassword == null)
                    return;

                String username = LogInSignUpActivity.extractString(mUsername, true);
                if (TextUtils.isEmpty(username)) {
                    mUsername.setError("Please enter username");
                    return;
                }

                String password = LogInSignUpActivity.extractString(mPassword);
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Please enter password");
                    return;
                }

                String verifyPassword = LogInSignUpActivity.extractString(mVerifyPassword);
                if (TextUtils.isEmpty(verifyPassword)) {
                    mVerifyPassword.setError("Please verify password");
                    return;
                }

                if (!verifyPassword.equals(password)) {
                    mVerifyPassword.setError("Passwords don't match");
                    return;
                }

                mSignUpListener.onSignUp(username, password);
            }
        });

        TextView bottomTxt = (TextView) rootView.findViewById(R.id.bottom_text);
        bottomTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignUpListener.onHaveAccountPressed();
            }
        });
        return rootView;
    }
}
