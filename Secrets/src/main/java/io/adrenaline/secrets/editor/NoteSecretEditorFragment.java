package io.adrenaline.secrets.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.NoteSecretModel;

/**
 * Created by stang6 on 11/8/13.
 */
public class NoteSecretEditorFragment extends SecretEditorFragment {
    private NoteSecretModel mNoteSecret;
    private TextView mNoteContent;

    public NoteSecretEditorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNoteSecret = (NoteSecretModel) getSecret();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        mNoteContent = (TextView) setEditorContent(R.layout.editor_note_content).findViewById(R.id.editor_note_content);
        mNoteContent.setText(mNoteSecret.getNote());

        return root;
    }

    @Override
    protected void save() {
        super.save();

        mNoteSecret.setNote(mNoteContent.getText().toString());
    }
}
