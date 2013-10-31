package io.adrenaline.secrets;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInFragment extends Fragment {
    OnCreateAccountPressedListener mCreateAccountListener;

    public interface OnCreateAccountPressedListener {
        public void onCreateAccountPressed();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCreateAccountListener = (OnCreateAccountPressedListener) activity;
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

        // tweak the text on the action button to reflect login instead
        // of account creation
        Button actionBtn = (Button) rootView.findViewById(R.id.create_account_log_in);
        actionBtn.setText(R.string.login);

        TextView bottomTxt = (TextView) rootView.findViewById(R.id.bottom_text);
        bottomTxt.setText(R.string.need_to_create_account);

        bottomTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCreateAccountListener.onCreateAccountPressed();
            }
        });

        EditText email = (EditText) rootView.findViewById(R.id.username);
        if (SecretsApplication.email() != null) {
            email.setText(SecretsApplication.email());
        }

        return rootView;
    }
}
