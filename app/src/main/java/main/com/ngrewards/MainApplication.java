package main.com.ngrewards;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.StartSliderAct;
import main.com.ngrewards.constant.MySession;

public class MainApplication extends Application {
    MySession mySession;

    @Override
    public void onCreate() {
        super.onCreate();
       }
}
