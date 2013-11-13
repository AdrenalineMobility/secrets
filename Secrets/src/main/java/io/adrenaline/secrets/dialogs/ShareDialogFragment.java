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
    private TextView mAddPersonField;
    private ListView mShareOption;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.share_group, null);
        mAddPersonField = (TextView) v.findViewById(R.id.add_person_field);
        mShareOption = (ListView) v.findViewById(R.id.sharing_options);
        String[] options = getActivity().getResources().getStringArray(R.array.sharing_options);
        mShareOption.setAdapter(new ArrayAdapter<CharSequence>(getActivity(), R.layout.share_options_item, R.id.sharing_option_text, options));
        mShareOption.setItemChecked(0, true);

        builder.setTitle(R.string.add_person);
        builder.setView(v);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
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