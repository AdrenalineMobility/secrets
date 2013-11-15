package io.adrenaline.secrets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import io.adrenaline.secrets.models.Secrets;
import io.adrenaline.secrets.views.GroupListEntryRelativeLayout;


/**
 * An activity representing a list of Groups. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link GroupDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link GroupListFragment} and the item details
 * (if present) is a {@link GroupDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link GroupListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class GroupListActivity extends FragmentActivity
        implements GroupListFragment.Callbacks, GroupListEntryRelativeLayout.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        if (findViewById(R.id.group_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((GroupListFragment) getFragmentManager()
                    .findFragmentById(R.id.group_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
        Secrets.sync();
    }

    /**
     * Callback method from {@link GroupListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     * @param index
     */
    @Override
    public void onItemSelected(int index) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(GroupDetailFragment.ARG_GROUP_INDEX, index);
            GroupDetailFragment fragment = new GroupDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.group_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, GroupDetailActivity.class);
            detailIntent.putExtra(GroupDetailFragment.ARG_GROUP_INDEX, index);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onEntrySelected(int index) {
        onItemSelected(index);
    }

    @Override
    public void onEntryInfoClicked(int index) {
        Bundle arguments = new Bundle();
        int container;
        if (mTwoPane) {
            // In two-pane mode, show the info view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            container = R.id.group_detail_container;
        } else {
            // In single-pane mode, put the info fragment
            // for the selected item ID in the list container.
            container = R.id.group_list_container;
        }
        arguments.putInt(GroupInfoFragment.ARG_CONTAINER_ID, container);
        arguments.putInt(GroupDetailFragment.ARG_GROUP_INDEX, index);
        GroupInfoFragment fragment = new GroupInfoFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction()
                .replace(container, fragment, GroupInfoFragment.TAG)
                .commit();

    }
}
