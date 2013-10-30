package io.adrenaline.secrets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.adrenaline.AdrenalineIo;

public class Splash extends Activity {
    private static final String TAG = "AdrenalineSecrets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // If we start with other activities, we should call this under Application.onCreate
        AdrenalineIo.init(getApplicationContext());

        final Button button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Splash.this, GroupListActivity.class));
            }
        });
    }
}