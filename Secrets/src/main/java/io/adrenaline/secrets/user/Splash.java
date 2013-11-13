package io.adrenaline.secrets.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.adrenaline.User;
import io.adrenaline.secrets.GroupListActivity;
import io.adrenaline.secrets.R;

public class Splash extends Activity {
    private static final String TAG = "AdrenalineSecrets";

    public final static String START_AS_LOGIN = "io.adrenaline.secrets.START_AS_LOGIN";

    @Override
    protected void onResume() {
        super.onResume();

        // if we're already logged in, go to group list activity
        if (User.getCurrentUser() != null) {
            Intent intent = new Intent(this, GroupListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        final Button button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Splash.this, GroupListActivity.class);
                intent.putExtra(START_AS_LOGIN, true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
            }
        });

        final Button signUpBtn = (Button) findViewById(R.id.sign_up_button);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Splash.this, LogInSignUpActivity.class);
                intent.putExtra(START_AS_LOGIN, false);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
            }
        });
    }
}
