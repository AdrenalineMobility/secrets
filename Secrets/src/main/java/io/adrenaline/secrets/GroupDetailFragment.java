package io.adrenaline.secrets;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import io.adrenaline.secrets.models.SecretGroupModel;
import io.adrenaline.secrets.models.Secrets;

/**
 * A fragment representing a single Group detail screen.
 * This fragment is either contained in a {@link GroupListActivity}
 * in two-pane mode (on tablets) or a {@link GroupDetailActivity}
 * on handsets.
 */
public class GroupDetailFragment extends Fragment {
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
}
