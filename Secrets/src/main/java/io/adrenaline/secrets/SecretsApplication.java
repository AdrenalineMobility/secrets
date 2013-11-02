package io.adrenaline.secrets;
import android.app.Application;

import io.adrenaline.AdrenalineIo;

public class SecretsApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        AdrenalineIo.init(getApplicationContext());

    }
}
