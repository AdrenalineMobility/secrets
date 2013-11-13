package io.adrenaline.secrets.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import io.adrenaline.secrets.R;

/**
 * Created by stang6 on 11/12/13.
 */
public class ShareDialogFragment extends DialogFragment {
    public static final String TAG = "SHARE_GROUP";
    public static final String USER_ID = "user_id";
    public static final String ACCESS = "access";

    private TextView mAddPersonField;
    private ListView mShareOption;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String userId = null;
        int access = 0;
        if (getArguments() != null) {
            userId = getArguments().getString(ShareDialogFragment.USER_ID);
            access = getArguments().getInt(ACCESS);
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.share_group, null);
        mAddPersonField = (TextView) v.findViewById(R.id.add_person_field);
        mShareOption = (ListView) v.findViewById(R.id.sharing_options);

        int okId;
        String[] options;
        if (userId == null) {
            builder.setTitle(R.string.add_person);
            okId = R.string.add;
            options = getActivity().getResources().getStringArray(R.array.sharing_options);
        } else {
            mAddPersonField.setVisibility(View.GONE);
            builder.setTitle(R.string.sharing_setting);
            okId = android.R.string.ok;
            options = getActivity().getResources().getStringArray(R.array.user_sharing_options);
        }

        mShareOption.setAdapter(new ArrayAdapter<CharSequence>(getActivity(), R.layout.share_options_item, R.id.sharing_option_text, options));
        mShareOption.setItemChecked(access, true);
        builder.setView(v);
        builder.setPositiveButton(okId, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Create the AlertDialog object and return it
        final Dialog dialog = builder.create();

        mAddPersonField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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