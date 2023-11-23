package main.com.ngrewards.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.app.Config;
import main.com.ngrewards.activity.app.NotificationUtils;
import main.com.ngrewards.bottumtab.MainTabActivity;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.marchant.merchantbottum.MerHomeActivity;
import main.com.ngrewards.marchant.merchantbottum.MerchantBottumAct;

public class SplashActivity extends AppCompatActivity implements
        //http://main.com.ngrewards/?productId=1

        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {

    public static final int RequestPermissionCode = 1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected static final String TAG = "MainActivityDummy";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private static final int SPLASH_TIME_OUT = 3500;
    public static double latitude = 0.0, longitude = 0.0;
    private final String mutableList = "test";
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;
    MySession mySession;
    GPSTracker gpsTracker;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    private String user_type = "";
    private Uri uri;
    private String hasData;
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PreferenceConnector.writeString(SplashActivity.this, PreferenceConnector.Logout_Status, "false");
        mySession = new MySession(this);
        gpsTracker = new GPSTracker(SplashActivity.this);
        mRequestingLocationUpdates = false;
        mySession = new MySession(this);
        Tools.updateResources(this, mySession.getValueOf(MySession.KEY_LANGUAGE));

        String newMutableList = "Test";

        Log.d(TAG, "MutableListComparison: " + mutableList.equals(newMutableList));
        try {
//    Log.e(TAG, "onCreate: ", new Throwable());
            FirebaseApp.initializeApp(this);
            FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, pendingDynamicLinkData -> {
                Uri deepLink = null;
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.getLink();
                    Log.d(TAG, "onSuccess:Deeplink " + deepLink);
                }
                Log.e("deepLink >>", " >" + deepLink);
                result = deepLink + "";
                String param = deepLink + "";
                if (param.contains("https://www.ngrewards.com/data/Ng?")) {
                    result = param.replace("https://www.ngrewards.com/data/Ng?", "");
                    result = "item" + result;
                } else if (param.contains("https://www.ngrewards.com/data/Merchent?")) {
                    result = param.replace("https://www.ngrewards.com/data/Merchent?", "");
                    result = "merchant" + result;
                } else if (param.contains("https://www.ngrewards.com/data/Offer?")) {
                    result = param.replace("https://www.ngrewards.com/data/Offer?", "");
                    result = "offer" + result;
                } else {
                    if (param.contains("https://www.ngrewards.com%2Fdata/Ng?")) {
                        result = param.replace("https://www.ngrewards.com%2Fdata/Ng?", "");
                        result = "item" + result;
                        Log.d(TAG, "onSuccess: " + result);
                    } else if (param.contains("https://www.ngrewards.com%2Fdata/Merchent?")) {
                        result = param.replace("https://www.ngrewards.com%2Fdata/Merchent?", "");
                        result = "merchant" + result;
                    } else if (param.contains("https://www.ngrewards.com%2Fdata/Offer?")) {
                        result = param.replace("https://www.ngrewards.com%2Fdata/Offer?", "");
                        result = "offer" + result;
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("getDynamicLink", "getDynamicLink:onFailure", e);
                    Log.e("deepLink >>Error", " >" + e);
                }
            });

            uri = getIntent().getData();

            // checking if the uri is null or not.
            if (uri != null) {

                // if the uri is not null then we are getting
                // the path segments and storing it in list.
                List<String> parameters = uri.getPathSegments();

                // after that we are extracting string
                // from that parameters.
                String param = parameters.get(parameters.size() - 1);

                // on below line we are setting that string
                // to our text view which we got as params.

                result = param.replaceAll("product_id=", "");

                Log.d(TAG, "onCreate: " + param);

            }

            String user_log_data = mySession.getKeyAlldata();
            Log.e("SPLASH user_log_data >>", " .." + user_log_data);
            if (user_log_data == null) {

            } else {

                try {
                    JSONObject jsonObject = new JSONObject(user_log_data);
                    String message = jsonObject.getString("status");
                    if (message.equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        user_type = jsonObject1.getString("user_type");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            mLastUpdateTime = "";
            updateValuesFromBundle(savedInstanceState);
            buildGoogleApiClient();
            createLocationRequest();
            buildLocationSettingsRequest();

            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                        displayFirebaseRegId();
                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        String message = intent.getStringExtra("message");
                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    }
                }
            };

            if (checkPermission()) {
                new Handler().postDelayed(() -> checkLocationSettings(), SPLASH_TIME_OUT);

            } else {
                requestPermission();
            }


            byte[] sha1 = {(byte) 0xA8, (byte) 0x41, (byte) 0x5E, (byte) 0xCF, (byte) 0x95, (byte) 0x58, (byte) 0x0D, (byte) 0x9C, (byte) 0x16, (byte) 0xFD, (byte) 0x89, (byte) 0x76, (byte) 0x95, 0x34, (byte) 0x2E, (byte) 0x49, (byte) 0x03, (byte) 0x6D, (byte) 0x36, (byte) 0x3E
                    //SHA1: A8:41:5E:CF:95:58:0D:9C:16:FD:89:76:95:34:2E:49:03:6D:36:3E
                    // C6:CC:60:1E:56:83:97:0A:35:D8:2C:0F:F7:F0:A3:A7:92:B4:A6:9B
            };

            byte[] sha2 = {0x4A, (byte) 0x40, (byte) 0xA8, (byte) 0x17, (byte) 0x6C, (byte) 0x55, (byte) 0xB1, (byte) 0x54, (byte) 0x79, (byte) 0xCC, (byte) 0x4D, (byte) 0xF2, (byte) 0xE2, (byte) 0xFA, (byte) 0x8F, (byte) 0x1B, (byte) 0x63, (byte) 0x5D, (byte) 0xE5, (byte) 0x70

                    // SHA1: 4A:40:A8:17:6C:55:B1:54:79:CC:4D:F2:E2:FA:8F:1B:63:5D:E5:70
                    // C6:CC:60:1E:56:83:97:0A:35:D8:2C:0F:F7:F0:A3:A7:92:B4:A6:9B

            };

            System.out.println("keyhashGooglePlaySignIn1:" + Base64.encodeToString(sha1, Base64.NO_WRAP));
            System.out.println("keyhashGooglePlaySignIn2:" + Base64.encodeToString(sha2, Base64.NO_WRAP));

            Log.e("sha1keybinary", Base64.encodeToString(sha1, Base64.NO_WRAP));

            try {
                @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo("main.com.ngrewards", PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());

                    Log.e("keyhashh", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }

            } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

            }

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return session.isValid();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nesePermission() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SplashActivity.this);
        alertDialog.setTitle(getResources().getString(R.string.allowpermission));
        alertDialog.setMessage(getResources().getString(R.string.nessper));
        alertDialog.setPositiveButton(getResources().getString(R.string.allow), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (checkPermission()) {
                } else {
                    requestPermission();
                }
            }
        });

        alertDialog.setNegativeButton(getResources().getString(R.string.closeapp), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String firebase_regid = pref.getString("regId", null);
        Log.e("Splash", "Firebase reg id: " + firebase_regid);
    }


    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES);
            int camera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
            int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_NETWORK_STATE);
            int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
            int FifthPermissionResult2 = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS);
            int FifthPermissionResult3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO);
            return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && camera == PackageManager.PERMISSION_GRANTED && ThirdPermissionResult == PackageManager.PERMISSION_GRANTED && FourthPermissionResult == PackageManager.PERMISSION_GRANTED && FifthPermissionResult == PackageManager.PERMISSION_GRANTED && FifthPermissionResult2 == PackageManager.PERMISSION_GRANTED&& FifthPermissionResult3 == PackageManager.PERMISSION_GRANTED;
        } else {
            int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int camera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
            int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_NETWORK_STATE);
            int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
            return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED && camera == PackageManager.PERMISSION_GRANTED && ThirdPermissionResult == PackageManager.PERMISSION_GRANTED && FourthPermissionResult == PackageManager.PERMISSION_GRANTED && FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_VIDEO}, RequestPermissionCode);
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                    , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , android.Manifest.permission.CAMERA
                    , android.Manifest.permission.ACCESS_NETWORK_STATE
                    , android.Manifest.permission.ACCESS_COARSE_LOCATION
                    , android.Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 33) {

            if (requestCode == RequestPermissionCode) {
                if (grantResults.length > 0) {
                    boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean camera = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean network = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean coarseloc = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean fineloc = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean dd = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    boolean dd2 = grantResults[6] == PackageManager.PERMISSION_GRANTED;

                    if (write && camera && network && coarseloc && fineloc && dd && dd2) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (mySession.IsLoggedIn()) {
                                    if (user_type.equalsIgnoreCase("Merchant")) {
                                       // Intent i = new Intent(SplashActivity.this, MerchantBottumAct.class);
                                        Intent i = new Intent(SplashActivity.this, MerHomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Intent i = new Intent(SplashActivity.this, MainTabActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                } else {
                                    Intent i = new Intent(SplashActivity.this, StartSliderAct.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }, SPLASH_TIME_OUT);

                    } else {
                        nesePermission();
                    }
                }
            }
        } else {

            if (requestCode == RequestPermissionCode) {
                if (grantResults.length > 0) {
                    boolean read = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean write = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean network = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean coarseloc = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean fineloc = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                    if (read && write && camera && network && coarseloc && fineloc) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (mySession.IsLoggedIn()) {
                                    if (user_type.equalsIgnoreCase("Merchant")) {
                                        // Intent i = new Intent(SplashActivity.this, MerchantBottumAct.class);
                                        Intent i = new Intent(SplashActivity.this, MerHomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Intent i = new Intent(SplashActivity.this, MainTabActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                } else {
                                    Intent i = new Intent(SplashActivity.this, StartSliderAct.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }, SPLASH_TIME_OUT);

                    } else {
                        Log.e(TAG, "onRequestPermissionsResult: " + grantResults.toString());
                        //  nesePermission();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (mySession.IsLoggedIn()) {
                                    if (user_type.equalsIgnoreCase("Merchant")) {
                                        // Intent i = new Intent(SplashActivity.this, MerchantBottumAct.class);
                                        Intent i = new Intent(SplashActivity.this, MerHomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Intent i = new Intent(SplashActivity.this, MainTabActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                } else {
                                    Intent i = new Intent(SplashActivity.this, StartSliderAct.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }, SPLASH_TIME_OUT);
                    }
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        if (mCurrentLocation == null) {

        } else {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
            Log.e("Lat or Long", " >> " + latitude + " >>" + longitude);
        }

    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(KEY_REQUESTING_LOCATION_UPDATES);
            }
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
        result.setResultCallback(this);
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();

                if (mySession.IsLoggedIn()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mCurrentLocation == null) {
                                if (gpsTracker.canGetLocation()) {
                                    latitude = gpsTracker.getLatitude();
                                    longitude = gpsTracker.getLongitude();
                                }
                            } else {
                                latitude = mCurrentLocation.getLatitude();
                                longitude = mCurrentLocation.getLongitude();
                            }
                            Log.e(TAG, "user_typeuser_typeuser_typeuser_typeuser_type: " + user_type);
                            if (user_type.equalsIgnoreCase("Merchant")) {
                                // Intent i = new Intent(SplashActivity.this, MerchantBottumAct.class);
                                Intent i = new Intent(SplashActivity.this, MerHomeActivity.class);
                                startActivity(i);
                                finish();

                            } else {
                                Intent i = new Intent(SplashActivity.this, MainTabActivity.class).putExtra("result", result);
                                startActivity(i);
                                finish();
                                PreferenceConnector.writeString(SplashActivity.this, PreferenceConnector.Status_Facebook, "");
                            }
                        }
                    }, 1500);

                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mCurrentLocation == null) {
                                if (gpsTracker.canGetLocation()) {
                                    latitude = gpsTracker.getLatitude();
                                    longitude = gpsTracker.getLongitude();
                                }
                            } else {
                                latitude = mCurrentLocation.getLatitude();
                                longitude = mCurrentLocation.getLongitude();
                            }
                            Intent i = new Intent(SplashActivity.this, StartSliderAct.class);
                            startActivity(i);
                            finish();

                        }
                    }, 1500);

                }

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");

                // try {
                try {
                    status.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }
                // } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "PendingIntent unable to execute request.");
                //  }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check for the integer request code originally supplied to startResolutionForResult().
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.e(TAG, "User agreed to make required location settings changes.");
                    if (mySession.IsLoggedIn()) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mCurrentLocation == null) {

                                } else {
                                    latitude = mCurrentLocation.getLatitude();
                                    longitude = mCurrentLocation.getLongitude();
                                }

                                // Intent i = new Intent(SplashAct.this, CheckLocationActivity.class);
                                if (user_type.equalsIgnoreCase("Merchant")) {

                                    // Intent i = new Intent(SplashActivity.this, MerchantBottumAct.class);
                                    Intent i = new Intent(SplashActivity.this, MerHomeActivity.class);
                                    startActivity(i);
                                    finish();

                                } else {

                                    Intent i = new Intent(SplashActivity.this, MainTabActivity.class);
                                    startActivity(i);
                                    finish();
                                    PreferenceConnector.writeString(SplashActivity.this, PreferenceConnector.Status_Facebook, "");
                                }

                            }
                        }, 1500);

                    } else {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mCurrentLocation == null) {

                                } else {
                                    latitude = mCurrentLocation.getLatitude();
                                    longitude = mCurrentLocation.getLongitude();
                                }

                                Intent i = new Intent(SplashActivity.this, StartSliderAct.class);
                                startActivity(i);
                                finish();

                            }
                        }, 1500);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e(TAG, "User chose not to make required location settings changes.");
                    finish();
                    break;
            }
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this).setResultCallback(status -> {
            mRequestingLocationUpdates = true;
            //  setButtonsEnabledState();

        });
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
                //  setButtonsEnabledState();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
}


//https://ngrewards.page.link
//Add dropdown menu below Gender section named “Age” with options:
/*
Member login
 Number = 00000 , user name = vijayshyam , pass 12345

 Merchant login
 Number = 11111 ,  , pass 12345
*/
//paypal demo account
//kapilsthapak67@gmail.com
//k@pil10392