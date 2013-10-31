package io.adrenaline.secrets;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class LogInSignUpActivity extends ActionBarActivity
        implements LogInFragment.OnCreateAccountPressedListener,
        SignUpFragment.OnHaveAccountPressedListener {

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
            getFragmentManager().beginTransaction()
                    .add(R.id.login_signup_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
    }

    private void switchFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                .setCustomAnimations(
                        R.anim.card_flip_left_in, R.anim.card_flip_left_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.login_signup_fragment_container, fragment)

                        // Add this transaction to the back stack, allowing users to press Back
                        // to get to the front of the card.
                .addToBackStack(null)

                        // Commit the transaction.
                .commit();
    }

    @Override
    public void onCreateAccountPressed() {
        switchFragment(new SignUpFragment());
    }

    @Override
    public void onHaveAccountPressed() {
        switchFragment(new LogInFragment());
    }
}
