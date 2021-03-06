package io.adrenaline.secrets.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.NoteSecretModel;
import io.adrenaline.secrets.models.SecretModel;

/**
 * Created by stang6 on 11/4/13.
 */
public class NoteSecretEntryRelativeLayout extends SecretEntryRelativeLayout {

    private NoteSecretModel mNote;
    private TextView mNoteView;

    public NoteSecretEntryRelativeLayout(Context context) {
        super(context);
    }

    public NoteSecretEntryRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteSecretEntryRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mNoteView = (TextView) addDetailView(R.layout.note_secret_detail).findViewById(R.id.secret_note);
    }

    @Override
    public void update(SecretModel note) {
        super.update(note);

        mNote = (NoteSecretModel) note;
        mNoteView.setText(mNote.getNote());
    }

}
