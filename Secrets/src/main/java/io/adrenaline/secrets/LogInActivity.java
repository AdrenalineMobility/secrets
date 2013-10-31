package io.adrenaline.secrets;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class LogInActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_log_in);

        // tweak the text on the action button to reflect login instead
        // of account creation
        Button actionBtn = (Button) findViewById(R.id.create_account_log_in);
        actionBtn.setText(R.string.login);
    }

}
