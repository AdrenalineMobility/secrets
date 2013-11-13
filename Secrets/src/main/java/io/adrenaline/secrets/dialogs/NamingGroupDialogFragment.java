package io.adrenaline.secrets.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import io.adrenaline.secrets.GroupDetailFragment;
import io.adrenaline.secrets.GroupInfoFragment;
import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.SecretGroupModel;
import io.adrenaline.secrets.models.Secrets;

/**
 * Created by stang6 on 11/7/13.
 */
public class NamingGroupDialogFragment extends DialogFragment {
    public static final String TAG = "GROUP_NAME";
    public static final String GROUP_INDEX = "group_index";

    private int mIndex = -1;

    public interface Callback {
        public void onNameChanged();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mIndex = getArguments().getInt(GROUP_INDEX, mIndex);
        }
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View content = inflater.inflate(R.layout.dialog_naming_group_edittext, null);
        builder.setView(content);
        final TextView groupName = (TextView) content.findViewById(R.id.group_name_field);

        if (mIndex < 0) {
            builder.setTitle(R.string.create_group);
        } else {
            builder.setTitle(R.string.change_group_name);
            groupName.setText(Secrets.getSecretGroup(mIndex).getName());
        }

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mIndex < 0) {
                    Secrets.addSecretGroup(new SecretGroupModel(groupName.getText().toString()));
                } else {
                    SecretGroupModel group = Secrets.getSecretGroup(mIndex);
                    group.setName(groupName.getText().toString());
                    Secrets.updateSecretGroup(group);

                    Fragment f = getFragmentManager().findFragmentByTag(GroupInfoFragment.TAG);
                    if (f instanceof Callback) {
                        ((Callback) f).onNameChanged();
                    }
                }
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog object and return it
        final Dialog dialog = builder.create();

        groupName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        return dialog;
    }

}
