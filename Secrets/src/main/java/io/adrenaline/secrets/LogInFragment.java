package io.adrenaline.secrets;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.adrenaline.AdrenalineIo;
import io.adrenaline.ApiResponse;

public class LogInFragment extends Fragment {
    private static final String TAG = "LogInFragment";
    private OnLogInListener mLogInListener;
    private EditText mUsername;
    private EditText mPassword;

    public interface OnLogInListener {
        public void onCreateAccountPressed();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mLogInListener = (OnLogInListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCreateAccountPressedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        if (rootView == null)
            return null;

        mUsername = (EditText) rootView.findViewById(R.id.username);
        mPassword = (EditText) rootView.findViewById(R.id.password);

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

        // tweak the text on the action button to reflect login instead
        // of account creation
        Button actionBtn = (Button) rootView.findViewById(R.id.create_account_log_in);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUsername == null || mPassword == null)
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

                onLogIn(username, password);
            }
        });

        TextView bottomTxt = (TextView) rootView.findViewById(R.id.bottom_text);
        bottomTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLogInListener.onCreateAccountPressed();
            }
        });

        return rootView;
    }

    private void onLogIn(final String username, String password) {
        final ProgressDialogFragment dialog = ProgressDialogFragment.showDialog(getActivity());
        dialog.setText("Logging in " + username + "...");
        mUsername.setError(null);
        mPassword.setError(null);

        AdrenalineAsync.logInAsync(getActivity(), username, password, new AdrenalineAsync.ApiDeferred() {
            @Override
            public void done(ApiResponse response) {
                Log.d(TAG, "Logged In!");
                dialog.dismiss();
                if (getActivity() != null)
                    getActivity().finish();
            }
            @Override
            public void fail(ApiResponse response) {
                dialog.dismiss();

                if (response.status().equals(AdrenalineIo.ERROR_INCORRECT_PASSWORD)) {
                    mPassword.setError("Incorrect password");
                    mPassword.requestFocus();
                } else if (response.status().equals(AdrenalineIo.ERROR_PUBLIC_KEY_NOT_FOUND)) {
                    mUsername.setError("User not found");
                    mUsername.requestFocus();
                } else {
                    String err = "Could not log in: " + response.status();
                    Log.e(TAG, err);
                    Activity activity = getActivity();
                    if (activity != null && activity.getApplicationContext() != null) {
                        Toast.makeText(activity.getApplicationContext(), err, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
