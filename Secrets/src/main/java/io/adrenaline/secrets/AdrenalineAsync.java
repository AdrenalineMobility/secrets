package io.adrenaline.secrets;


import android.app.Activity;
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

    private static class LogInSignUpAsync extends Thread {
        private ApiDeferred mDeferred;
        private boolean mIsLogIn;
        private String mUsername;
        private String mPassword;
        private Activity mActivity;

        public LogInSignUpAsync(Activity activity, String username, String password, ApiDeferred deferred, boolean isLogIn) {
            if (deferred == null) {
                mDeferred = new ApiDeferred();
            } else {
                mDeferred = deferred;
            }
            mIsLogIn = isLogIn;
            mUsername = username;
            mPassword = password;
            mActivity = activity;
        }

        @Override
        public void run() {
            ApiResponse response;
            if (mIsLogIn) {
                response = User.logIn(mUsername, mPassword);
            } else {
                response = User.signUp(mUsername, mPassword);
            }

            mLock.lock();
            mRequestInFlight = false;
            mLock.unlock();

            final ApiResponse resp = response;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!resp.ok()) {
                        mDeferred.fail(resp);
                    } else {
                        mDeferred.done(resp);
                    }
                }
            });
        }
    }

    private static boolean logInSignUp(Activity activity, String username, String password, ApiDeferred deferred, boolean isLogIn) {
        mLock.lock();
        if (mRequestInFlight) {
            mLock.unlock();
            Log.e(TAG, "ERROR!!! you tried to login/signup when there was already a login/signup request in flight");
            return false;
        }
        new LogInSignUpAsync(activity, username, password, deferred, isLogIn).start();
        mRequestInFlight = true;
        mLock.unlock();

        return true;
    }

    public static boolean logInAsync(Activity activity, String username, String password, ApiDeferred deferred) {
        return logInSignUp(activity, username, password, deferred, true);
    }
    public static boolean signUpAsync(Activity activity, String username, String password, ApiDeferred deferred) {
        return logInSignUp(activity, username, password, deferred, false);
    }
}
