package io.adrenaline.secrets;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import io.adrenaline.secrets.editor.SecretEditorActivity;
import io.adrenaline.secrets.editor.SecretEditorFragment;
import io.adrenaline.secrets.models.NoteSecretModel;
import io.adrenaline.secrets.models.SecretGroupModel;
import io.adrenaline.secrets.models.SecretModel;
import io.adrenaline.secrets.models.Secrets;
import io.adrenaline.secrets.views.SecretEntryRelativeLayout;

/**
 * A fragment representing a single Group detail screen.
 * This fragment is either contained in a {@link GroupListActivity}
 * in two-pane mode (on tablets) or a {@link GroupDetailActivity}
 * on handsets.
 */
public class GroupDetailFragment extends Fragment implements SecretEntryRelativeLayout.Callbacks {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_GROUP_INDEX = "group_index";

    private SecretGroupModel mSecretGroup;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_GROUP_INDEX)) {
            // Load the group content specified by the fragment
            mSecretGroup = Secrets.getSecretGroup(getArguments().getInt(ARG_GROUP_INDEX));
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group_detail, container, false);

        // Show the dummy content as text in a TextView.
        ListView listView = (ListView) rootView.findViewById(R.id.group_detail);
        if (mSecretGroup != null) {
            listView.setAdapter(mSecretGroup.getAdapter());
        }

        return rootView;
    }

    private void openSecretEditor(SecretModel secret) {
        Intent detailIntent = new Intent(this.getActivity(), SecretEditorActivity.class);
        detailIntent.putExtra(GroupDetailFragment.ARG_GROUP_INDEX, Secrets.indexOfSecretGroup(mSecretGroup));
        detailIntent.putExtra(SecretEditorFragment.ARG_SECRET_INDEX, mSecretGroup.indexOfSecret(secret));
        startActivity(detailIntent);
    }

    private void createNoteSecret() {
        NoteSecretModel noteSecret = new NoteSecretModel();
        mSecretGroup.addSecret(noteSecret);
        openSecretEditor(noteSecret);
    }

    private void createPasswordSecret() {

    }


    private void openInfoPanel() {
        Bundle arguments = new Bundle();
        arguments.putInt(GroupInfoFragment.ARG_CONTAINER_ID, R.id.group_detail_container);
        arguments.putInt(GroupDetailFragment.ARG_GROUP_INDEX, Secrets.indexOfSecretGroup(mSecretGroup));
        GroupInfoFragment fragment = new GroupInfoFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out)
                .add(R.id.group_detail_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_note_secret:
                createNoteSecret();
                return true;
            case R.id.action_add_password_secret:
                return true;
            case R.id.action_general_info:
                openInfoPanel();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onEditSecretClicked(SecretModel secret) {
        openSecretEditor(secret);
    }
}
