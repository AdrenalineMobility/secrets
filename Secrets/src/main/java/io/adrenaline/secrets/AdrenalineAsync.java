package io.adrenaline.secrets;


import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.adrenaline.ApiResponse;
import io.adrenaline.User;

public class AdrenalineAsync {
    private static final String TAG = "AdrenalineAsync";
    private static boolean mRequestInFlight = false;
    private static Lock mLock = new ReentrantLock();

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
            mLock.lock();
            mRequestInFlight = false;
            mLock.unlock();

            if (!response.ok()) {
                mDeferred.fail(response);
            } else {
                mDeferred.done(response);
            }
        }
    }

    private static boolean logInSignUp(String username, String password, ApiDeferred deferred, boolean isLogIn) {
        mLock.lock();
        if (mRequestInFlight) {
            mLock.unlock();
            Log.e(TAG, "ERROR!!! you tried to login/signup when there was already a login/signup request in flight");
            return false;
        }
        new LogInSignUpAsync(deferred, isLogIn).execute(username, password);
        mRequestInFlight = true;
        mLock.unlock();

        return true;
    }

    public static boolean logInAsync(String username, String password, ApiDeferred deferred) {
        return logInSignUp(username, password, deferred, true);
    }
    public static boolean signUpAsync(String username, String password, ApiDeferred deferred) {
        return logInSignUp(username, password, deferred, false);
    }
}
