package io.adrenaline.secrets;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LogInFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_signup, container, false);

        if (rootView == null)
            return null;

        // tweak the text on the action button to reflect login instead
        // of account creation
        Button actionBtn = (Button) rootView.findViewById(R.id.create_account_log_in);
        actionBtn.setText(R.string.login);

        TextView bottomTxt = (TextView) rootView.findViewById(R.id.bottom_text);
        bottomTxt.setText(R.string.need_to_create_account);

        return rootView;
    }
}
