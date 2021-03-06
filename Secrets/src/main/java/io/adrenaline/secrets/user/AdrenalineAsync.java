package io.adrenaline.secrets.user;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.adrenaline.ApiResponse;
import io.adrenaline.User;
import io.adrenaline.secrets.models.Secrets;

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

        public LogInSignUpAsync(String username, String password, ApiDeferred deferred, boolean isLogIn) {
            if (deferred == null) {
                mDeferred = new ApiDeferred();
            } else {
                mDeferred = deferred;
            }
            mIsLogIn = isLogIn;
            mUsername = username;
            mPassword = password;
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
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!resp.ok()) {
                        mDeferred.fail(resp);
                    } else {
                        Secrets.sync();
                        mDeferred.done(resp);
                    }
                }
            });
        }
    }

    private static boolean logInSignUp(String username, String password, ApiDeferred deferred, boolean isLogIn) {
        mLock.lock();
        if (mRequestInFlight) {
            mLock.unlock();
            Log.e(TAG, "ERROR!!! you tried to login/signup when there was already a login/signup request in flight");
            return false;
        }
        new LogInSignUpAsync(username, password, deferred, isLogIn).start();
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

    public static void lookupUser(final String userId, final ApiDeferred deferred) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ApiResponse response = User.fetchUser(userId);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!response.ok()) {
                            deferred.fail(response);
                        } else {
                            deferred.done(response);
                        }
                    }
                });
            }
        }).start();
    }
}
