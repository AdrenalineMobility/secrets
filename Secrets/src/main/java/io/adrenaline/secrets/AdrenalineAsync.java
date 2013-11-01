package io.adrenaline.secrets;


import android.os.AsyncTask;

import io.adrenaline.ApiResponse;
import io.adrenaline.User;

public class AdrenalineAsync {
    public static class ApiDeferred {
        public void onProgress(String progress){}
        public void done(ApiResponse response){}
        public void fail(ApiResponse response){}
    }

    private static class LogInSignUpAsync extends AsyncTask<String, Void, ApiResponse> {
        private ApiDeferred mDeferred;
        private boolean mIsLogIn;
        public LogInSignUpAsync(ApiDeferred deferred, boolean isLogIn) {
            if (deferred == null) {
                mDeferred = new ApiDeferred();
            } else {
                mDeferred = deferred;
            }
            mIsLogIn = isLogIn;
        }
        @Override
        protected ApiResponse doInBackground(String... arg0) {
            String username = arg0[0];
            String password = arg0[1];
            ApiResponse response;
            if (mIsLogIn) {
                response = User.logIn(username, password);
            } else {
                response = User.signUp(username, password);
            }
            return response;
        }

        @Override
        protected void onPostExecute(ApiResponse response) {
            if (!response.ok()) {
                mDeferred.fail(response);
            } else {
                mDeferred.done(response);
            }
        }
    }

    public static void logInAsync(String username, String password, ApiDeferred deferred) {
        new LogInSignUpAsync(deferred, true).execute(username, password);
    }
    public static void signUpAsync(String username, String password, ApiDeferred deferred) {
        new LogInSignUpAsync(deferred, false).execute(username, password);
    }
}
