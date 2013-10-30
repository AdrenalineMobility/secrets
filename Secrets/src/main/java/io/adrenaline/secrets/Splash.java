package io.adrenaline.secrets;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import io.adrenaline.AdrenalineIo;
import io.adrenaline.ApiResponse;

public class Splash extends ActionBarActivity {
    private static final String TAG = "AdrenalineSecrets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        AdrenalineIo.init(getApplicationContext());

        final TextView button = (TextView) findViewById(R.id.sign_up_link);
        final Activity that = this;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(that, GroupListActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToActivity(View view) {
        Intent intent = new Intent(this, GroupListActivity.class);
        startActivity(intent);
        // Do something in response to button
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private class PingApiServer extends AsyncTask<TextView, Void, ApiResponse> {
            TextView status = null;

            @Override
            protected ApiResponse doInBackground(TextView... arg0) {
                status = arg0[0];
                ApiResponse resp = AdrenalineIo.getAppDetails();
                return resp;
            }

            @Override
            protected void onPostExecute(ApiResponse resp) {
                if (resp.ok()) {
                    status.setText("Connection Successful. Welcome to: " + resp.getString("name"));
                } else {
                    status.setText("Error -> " + resp.status());
                    Log.e(TAG, "Error -> " + resp.status());
                }
            }
        }


        public PlaceholderFragment() {
        }

        /*
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_splash, container, false);

            TextView status = (TextView) rootView.findViewById(R.id.helloWorld);
            status.setText("Checking server...");
            new PingApiServer().execute(status);

            return rootView;
        }
        */

    }
}