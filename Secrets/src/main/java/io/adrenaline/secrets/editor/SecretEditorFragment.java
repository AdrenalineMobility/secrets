package io.adrenaline.secrets.editor;

import android.app.Fragment;
import android.app.Service;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import io.adrenaline.secrets.GroupDetailFragment;
import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.SecretGroupModel;
import io.adrenaline.secrets.models.SecretModel;
import io.adrenaline.secrets.models.Secrets;

/**
 * Created by stang6 on 11/7/13.
 */
public abstract class SecretEditorFragment extends Fragment {

    public static final String ARG_SECRET_INDEX = "secret_index";

    private SecretModel mSecret;
    private TextView mTitleField;
    private ViewGroup mEditorContent;

    public SecretEditorFragment() {
    }

    protected final SecretModel getSecret() {
        if (getArguments().containsKey(GroupDetailFragment.ARG_GROUP_INDEX)
                && getArguments().containsKey(ARG_SECRET_INDEX)) {
            // Load the group content specified by the fragment
            SecretGroupModel group = Secrets.getSecretGroup(getArguments().getInt(GroupDetailFragment.ARG_GROUP_INDEX));
            return group.getSecret(getArguments().getInt(ARG_SECRET_INDEX));
        }
        return null;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SecretModel secret = getSecret();
        if (secret == null) {
            throw new IllegalStateException("Cannot open editor without content");
        }
        mSecret = secret;

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_secret_editor, container, false);

        mEditorContent = (ViewGroup) rootView.findViewById(R.id.editor_content);
        mTitleField = (TextView) rootView.findViewById(R.id.editor_title);
        mTitleField.setText(mSecret.getName());

        mTitleField.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return rootView;
    }

    protected final View setEditorContent(int resId) {
        return View.inflate(getActivity(), resId, mEditorContent);
    }

    @Override
    public void onStart() {
        super.onStart();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mTitleField, 0);
    }

    protected void save() {
        mSecret.setName(mTitleField.getText().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_discard_secret:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.secret_editor, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
