package main.com.ngrewards.activity.app;

/**
 * Created by technorizen on 7/9/17.
 */

public class Config {
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
    public static final String YOUTUBE_API_KEY = "AIzaSyBcD0RqGF9xFmZm7vLHPyyI4ko43kS_-uQ";
    //public static final String YOUTUBE_API_KEY = "AIzaSyCFHhMjw5838GOT0vVKTx9fh99W2Sf5eHI";
    // public static final String YOUTUBE_API_KEY = "AIzaSyDfcKSNSMZbJo_Hk4UcxID709zc921KGZs";

}
