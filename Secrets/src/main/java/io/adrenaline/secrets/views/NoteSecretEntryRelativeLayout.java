package io.adrenaline.secrets.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.models.NoteSecretModel;
import io.adrenaline.secrets.models.SecretModel;

/**
 * Created by stang6 on 11/4/13.
 */
public class NoteSecretEntryRelativeLayout extends SecretEntryRelativeLayout {

    private SecretModel mNote;

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
    }

    @Override
    public void update(SecretModel note) {
        super.update(note);

        mNote = note;
    }
}
