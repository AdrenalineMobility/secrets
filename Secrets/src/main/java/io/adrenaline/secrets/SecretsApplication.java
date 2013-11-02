package io.adrenaline.secrets;
import android.app.Application;

import io.adrenaline.AdrenalineIo;

public class SecretsApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        AdrenalineIo.init(getApplicationContext());

        // for our app
        AdrenalineIo.BASE_URL = "https://adrenaline-io.appspot.com/";
        AdrenalineIo.APP_ID = "dd812ad1f8938ccb6cb7e543662f9baa5d897188";
        AdrenalineIo.APP_TOKEN = "aJLVgYjZcwwpW52UpHj7MALdLZSUcQT2";
    }
}
