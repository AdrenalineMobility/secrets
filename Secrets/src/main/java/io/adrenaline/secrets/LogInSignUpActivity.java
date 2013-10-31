package io.adrenaline.secrets;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class LogInSignUpActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            boolean startAsLogin = intent.getBooleanExtra(Splash.START_AS_LOGIN, false);
            Fragment fragment;
            if (startAsLogin) {
                fragment = new LogInFragment();
            } else {
                fragment = new SignUpFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_signup_fragment_container, fragment)
                    .commit();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
    }
}
