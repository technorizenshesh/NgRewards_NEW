package main.com.ngrewards;

import android.app.Application;
import android.content.Context;


public class MainApplication extends Application {
    private MainApplication smsApp;

    public MainApplication getInstance() {
        return smsApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        smsApp =this;
       }


}
