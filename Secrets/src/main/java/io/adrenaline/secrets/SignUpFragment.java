package io.adrenaline.secrets;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.adrenaline.AdrenalineIo;
import io.adrenaline.ApiResponse;

public class SignUpFragment extends Fragment {
    private static final String TAG = "SignUpFragment";
    private OnSignUpListener mSignUpListener;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mVerifyPassword;

    public interface OnSignUpListener {
        public void onHaveAccountPressed();
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

        // clear the error message if the user starts to modify the username
        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                mUsername.setError(null);
            }
        });

        Button actionBtn = (Button) rootView.findViewById(R.id.create_account_log_in);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUsername == null || mPassword == null || mVerifyPassword == null)
                    return;

                String username = LogInSignUpActivity.extractString(mUsername, true);
                if (TextUtils.isEmpty(username)) {
                    mUsername.setError("Please enter username");
                    mUsername.requestFocus();
                    return;
                }

                String password = LogInSignUpActivity.extractString(mPassword);
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Please enter password");
                    mPassword.requestFocus();
                    return;
                }

                String verifyPassword = LogInSignUpActivity.extractString(mVerifyPassword);
                if (TextUtils.isEmpty(verifyPassword)) {
                    mVerifyPassword.setError("Please verify password");
                    mVerifyPassword.requestFocus();
                    return;
                }

                if (!verifyPassword.equals(password)) {
                    mVerifyPassword.setError("Passwords don't match");
                    mVerifyPassword.requestFocus();
                    return;
                }

                onSignUp(username, password);
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

    private void onSignUp(String username, String password) {
        final ProgressDialogFragment dialog = ProgressDialogFragment.showDialog(getActivity());
        dialog.setText("Signing up " + username + "...");

        AdrenalineAsync.signUpAsync(getActivity(), username, password, new AdrenalineAsync.ApiDeferred() {
            @Override
            public void done(ApiResponse response) {
                Log.d(TAG, "Signed up!");
                dialog.dismiss();
                if (getActivity() != null)
                    getActivity().finish();
            }
            @Override
            public void fail(ApiResponse response) {
                String err = "Could not sign up " + response.status();
                Log.e(TAG, err);
                dialog.dismiss();

                if (response.status().equals("error_user_exists")) {
                    mUsername.setError("Username already taken");
                    mUsername.requestFocus();
                } else {
                    Activity activity = getActivity();
                    if (activity != null && activity.getApplicationContext() != null)
                        Toast.makeText(activity.getApplicationContext(), err, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
