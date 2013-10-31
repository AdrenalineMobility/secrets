package io.adrenaline.secrets;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.util.Patterns;

import java.util.regex.Pattern;

import io.adrenaline.AdrenalineIo;

public class SecretsApplication extends Application {
    private static String mEmailAddress;

    @Override
    public void onCreate() {
        super.onCreate();

        AdrenalineIo.init(getApplicationContext());
        fetchEmail(getApplicationContext());
    }

    public static String email() {
        return mEmailAddress;
    }

    private void fetchEmail(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // from http://stackoverflow.com/questions/2112965/how-to-get-the-android-devices-primary-e-mail-address
                Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
                Account[] accounts = AccountManager.get(context).getAccounts();
                for (Account account : accounts) {
                    if (emailPattern.matcher(account.name).matches()) {
                        // don't worry about sync, all other uses are read only
                        mEmailAddress = account.name;
                        break;
                    }
                }
            }
        }).start();
    }
}
