package io.adrenaline.secrets.editor;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Service;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.SecretModel;

/**
 * Created by stang6 on 11/7/13.
 */
public abstract class SecretEditorFragment extends Fragment {

    private final SecretModel mSecret;
    private ActionMode mActionMode;
    private TextView mTitleField;
    private ViewGroup mEditorContent;

    public SecretEditorFragment(SecretModel secret) {
        mSecret = secret;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_secret_editor, container, false);

        mEditorContent = (ViewGroup) rootView.findViewById(R.id.editor_content);
        mTitleField = (TextView) rootView.findViewById(R.id.editor_title);

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

        if (mActionMode != null) {
            return;
        }
        // Start the CAB using the ActionMode.Callback defined below
        mActionMode = getActivity().startActionMode(mActionModeCallback);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mTitleField, 0);

        update();
    }

    protected void update() {
        mTitleField.setText(mSecret.getName());
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.secret_editor, menu);
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
                case R.id.action_discard_secret:
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

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            getActivity().getFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .remove(SecretEditorFragment.this).commit();
        }
    };
}
