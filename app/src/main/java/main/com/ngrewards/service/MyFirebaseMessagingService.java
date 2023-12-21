package main.com.ngrewards.service;

/**
 * Created by ritesh on 20/3/17.
 */

import static main.com.ngrewards.activity.app.NotificationUtils.isAppIsInBackground;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import main.com.ngrewards.activity.MemberChatAct;
import main.com.ngrewards.activity.NotificationActivity;
import main.com.ngrewards.activity.SplashActivity;
import main.com.ngrewards.activity.app.Config;
import main.com.ngrewards.activity.app.NotificationUtils;
import main.com.ngrewards.marchant.merchantbottum.MerchantBottumAct;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static String notification_data = "";
    private NotificationUtils notificationUtils;
    private String type = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        /*
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
*/
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean result = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && powerManager.isInteractive() || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH && powerManager.isScreenOn();
        if (!result) {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MH24_SCREENLOCK");
            wl.acquire(10000);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MH24_SCREENLOCK");
            wl_cpu.acquire(10000);
        } else {
            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
            wakeLock.acquire();
        }

        if (remoteMessage == null)
            return;
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e("remoteMessage>>>>", "" + remoteMessage);
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", String.valueOf(json));
                // sendBroadcast(pushNotification);
                //   LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!isAppIsInBackground(getApplicationContext())) {
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            // play notification sound
            /// NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        String format = "";
        notification_data = json.toString();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format = simpleDateFormat.format(new Date());
            Log.e(TAG, "push json: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject data = json.getJSONObject("message");
            String keyMessage = data.getString("key");
            type = data.getString("type");
            Log.e("ssagar>> ", keyMessage);
            // Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            // pushNotification.putExtra("message", data.toString());
            Intent intent = new Intent(Config.PUSH_NOTIFICATION);
            intent.putExtra("key", data.toString());
            sendBroadcast(intent);
            if (!isAppIsInBackground(getApplicationContext())) {

                Log.e(TAG, "handleDataMessage: isAppIsInBackground");
                if (!MemberChatAct.isInFront) {
                    Log.e(TAG, "handleDataMessage: MemberChatAct.isInFront");

                    if (keyMessage.equalsIgnoreCase("You have a new chat message")) {
                        if (data.getString("type").equalsIgnoreCase("Member")) {
                            Intent resultIntent = new Intent(getApplicationContext(), MemberChatAct.class);
                            resultIntent.putExtra("receiver_id", data.getString("userid"));
                            resultIntent.putExtra("type", data.getString("receiver_type"));
                            resultIntent.putExtra("receiver_type", data.getString("type"));
                            if (data.getString("receiver_type") != null && data.getString("receiver_type").equalsIgnoreCase("Member")) {
                                resultIntent.putExtra("receiver_fullname", data.getString("fullname"));
                            } else {
                                resultIntent.putExtra("receiver_fullname", data.getString("username"));
                            }
                            resultIntent.putExtra("receiver_img", data.getString("userimage"));
                            resultIntent.putExtra("receiver_name", data.getString("username"));
                            showNotificationMessage(getApplicationContext(), "NG Rewards", "" + keyMessage, format, resultIntent, null, type);
                        } else {
                            Intent resultIntent = new Intent(getApplicationContext(), MemberChatAct.class);
                            resultIntent.putExtra("receiver_id", data.getString("userid"));
                            resultIntent.putExtra("type", data.getString("receiver_type"));
                            resultIntent.putExtra("receiver_type", data.getString("type"));
                            resultIntent.putExtra("receiver_img", data.getString("userimage"));
                            resultIntent.putExtra("receiver_name", data.getString("username"));
                            resultIntent.putExtra("receiver_fullname", data.getString("fullname"));
                            showNotificationMessage(getApplicationContext(), "NG Rewards", "" + keyMessage, format, resultIntent, null, type);
                        }
                    } else {

                        if (keyMessage.contains("You have emi pending please pay now")) {
                            Log.e(TAG, "handleDataMessage: You have a new chat message");
                            Intent intent1 = new Intent("MEMBER_HOME");
                            Log.e("MEMBER_HOMEMEMBER_HOME", "MEMBER_HOMEMEMBER_HOMEMEMBER_HOME" + data);
                            intent1.putExtra("object", data.toString());
                            sendBroadcast(intent1);
                            showNotificationMessage(getApplicationContext(), "NG Rewards",
                                    "" + keyMessage, format,
                                    intent1, null, type);

                        } else {
                            Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                            showNotificationMessage(getApplicationContext(), "NG Rewards", "" + keyMessage, format, resultIntent, null, type);

                        }

                    }
                } else {
                    Log.e(TAG, "handleDataMessage: !MemberChatAct.isInFront");

                }

                Log.e("KEYKEYOUTSIDE ", " >>> " + keyMessage);

                if (keyMessage.equalsIgnoreCase("You Have a new Transfer Money Request")) {
                    Log.e("KEYKEYINSIDE ", " >>> " + keyMessage);
                    Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                    showNotificationMessage(getApplicationContext(), "NG Rewards", "" + keyMessage, format, resultIntent, null, type);
                } else if (keyMessage.equalsIgnoreCase("You have a new  message")) {
                    String message = data.getString("message");
                    Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                    showNotificationMessage(getApplicationContext(), "NG Rewards", "" + message, format, resultIntent, null, type);
                }

            } else {
                if (keyMessage.equalsIgnoreCase("You have a new chat message")) {
                    Log.e("IN Service ELSE", "KEY: " + keyMessage);
                    if (data.getString("type").equalsIgnoreCase("Member")) {
                        Intent resultIntent = new Intent(getApplicationContext(), MemberChatAct.class);
                        resultIntent.putExtra("receiver_id", data.getString("userid"));
                        resultIntent.putExtra("type", data.getString("receiver_type"));
                        if (data.getString("receiver_type") != null && data.getString("receiver_type").equalsIgnoreCase("Member")) {
                            resultIntent.putExtra("receiver_fullname", data.getString("fullname"));
                        } else {
                            resultIntent.putExtra("receiver_fullname", data.getString("username"));
                        }
                        resultIntent.putExtra("receiver_type", data.getString("type"));
                        resultIntent.putExtra("receiver_type", data.getString("receiver_type"));
                        resultIntent.putExtra("receiver_img", data.getString("userimage"));
                        resultIntent.putExtra("receiver_name", data.getString("username"));
                        showNotificationMessage(getApplicationContext(), "NG Rewards", "" + keyMessage, format, resultIntent, null, type);
                    } else {
                        Intent resultIntent = new Intent(getApplicationContext(), MemberChatAct.class);
                        resultIntent.putExtra("receiver_id", data.getString("userid"));
                        resultIntent.putExtra("type", data.getString("receiver_type"));
                        resultIntent.putExtra("receiver_type", data.getString("type"));
                        resultIntent.putExtra("receiver_img", data.getString("userimage"));
                        resultIntent.putExtra("receiver_name", data.getString("username"));
                        resultIntent.putExtra("receiver_fullname", data.getString("fullname"));
                        showNotificationMessage(getApplicationContext(), "NG Rewards", "" + keyMessage, format, resultIntent, null, type);
                    }
                } else if (keyMessage.equalsIgnoreCase("Your Number Used By Someone")) {
                    Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                    showNotificationMessage(getApplicationContext(), "NG Rewards", "" + keyMessage, format, resultIntent, null, type);
                } else if (keyMessage.equalsIgnoreCase("You Have a new Transfer Money Request")) {
                    Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                    showNotificationMessage(getApplicationContext(), "NG Rewards", "" + keyMessage, format, resultIntent, null, type);
                } else if (keyMessage.equalsIgnoreCase("Your Item purchased by member")) {
                    Intent resultIntent = new Intent(getApplicationContext(), MerchantBottumAct.class);
                    showNotificationMessage(getApplicationContext(), "NG Rewards", "" + keyMessage, format, resultIntent, null, type);
                } else if (keyMessage.equalsIgnoreCase("You have a new  message")) {
                    String message = data.getString("message");
                    String type = data.getString("type");
                    Intent resultIntent = new Intent(getApplicationContext(), MerchantBottumAct.class);
                    showNotificationMessage(getApplicationContext(), "NG Rewards", "" + message, format, resultIntent, null, type);
                } else {
                    Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                    showNotificationMessage(getApplicationContext(), "NG Rewards", "" + keyMessage, format, resultIntent, null, type);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
            Log.e(TAG, "Json Exception: " + e.getLocalizedMessage());
            Log.e(TAG, "Json Exception: " + e.getCause());
        } catch (Exception e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
            Log.e(TAG, "Json Exception: " + e.getLocalizedMessage());
            Log.e(TAG, "Json Exception: " + e.getCause());
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String
            timeStamp, Intent intent, String route_img, String type) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage1(title, message, timeStamp, intent, route_img, type);

      /*  final int not_nu = generateRandom();
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this, getString(R.string.channelId))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(contentIntent)
                .setVibrate(new long[]{1000, 1000});
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.channelId), "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            noBuilder.setChannelId(getString(R.string.channelId));
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(not_nu, noBuilder.build());*/

    }

    public int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", s);
        editor.commit();
        Log.d("onNewToken()=>", "onNewTokenonNewTokenonNewTokenonNewToken----" + pref.getString("regId",""));

    }
}