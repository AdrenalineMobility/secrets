package io.adrenaline.secrets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LogInActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_log_in);

        // tweak the text on the action button to reflect login instead
        // of account creation
        Button actionBtn = (Button) findViewById(R.id.create_account_log_in);
        actionBtn.setText(R.string.login);

        TextView bottomTxt = (TextView) findViewById(R.id.bottom_text);
        bottomTxt.setText(R.string.need_to_create_account);
        bottomTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
    }
}
