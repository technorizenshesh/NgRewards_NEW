package main.com.ngrewards;


import android.app.Application;
import android.content.Context;

import main.com.ngrewards.activity.app.Config;

public class MainApplication extends Application {
     MainApplication smsApp;
     Context context;

    public MainApplication getInstance() {
        return smsApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        smsApp = this;
        context = getBaseContext();

    }


}
