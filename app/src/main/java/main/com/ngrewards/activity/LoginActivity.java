package main.com.ngrewards.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.app.Config;
import main.com.ngrewards.bottumtab.MainTabActivity;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.CountryBean;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.marchant.MarchantLogin;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout backlay;
    private TextView bytaping, dontaccount, login_tv, businesslogin;
    private EditText mobilenum, password_et;
    private String mobilenum_str = "", password_str = "", firebase_regid = "";
    public static String country_str = "", country_id = "";
    private TextView next_tv, forgot_tv, login_with_touch;
    MySession mySession;
    Myapisession myapisession;
    private ProgressBar progresbar;
    private LoginButton loginButton;
    private LinearLayout facebook_button;
    String facebook_name, facebook_email, facebook_id, facebook_image,
            face_gender, face_locale, facebook_lastname = "", face_username;
    private CallbackManager callbackManager;
    private Spinner country_spn;
    CountryListAdapter countryListAdapter;
    private ArrayList<CountryBean> countryBeanArrayList;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0;
    LocationManager locationManager;
    Location location;
    private double latitude = 0, longitude = 0;
    GPSTracker gpsTracker;
    private TextView sss;
    KeyStore keyStore;
    String KEY_NAME = "NgRewards";
    Cipher cipher;
    TextView textView;
    private AccessToken accessToken;
    private String result;
    private String phone;
    private String invited_user_name;
    private String facebook_phone;
    private String facebook_user_invited;
    private String fullname;
    private String newLogoutStatus;
    private TextView privacy_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(LoginActivity.this);

        PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.Logout_Status, "true");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newLogoutStatus = "";
            } else {
                newLogoutStatus = extras.getString("logout_status");
            }
        } else {
            newLogoutStatus = (String) savedInstanceState.getSerializable("logout_status");
        }

        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.e("applog", FacebookSdk.getApplicationSignature(LoginActivity.this));
        myapisession = new Myapisession(this);
        mySession = new MySession(this);
        callbackManager = CallbackManager.Factory.create();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("printHashKey", hashKey);
            }
        } catch (Exception e) {
            Log.e("printHashKey", String.valueOf(e));
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo("main.com.ngrewards", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                Log.e("mdkeyhashh:", String.valueOf(md));
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        mySession.signinusers(false);
        myapisession.setKeyAddressdata("");
        myapisession.setKeyCartitem("");
        myapisession.setProductdata("");
        myapisession.setKeyOffercate("");

        if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        checkGps();
        idint();
        clickevet();


            new GetCountryList().execute();
    }

    private void clickevet() {

        country_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (countryBeanArrayList != null && !countryBeanArrayList.isEmpty()) {
                    country_str = countryBeanArrayList.get(position).getName();
                    country_id = countryBeanArrayList.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        login_tv.setOnClickListener(v -> {

            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
            firebase_regid = pref.getString("regId", null);

            Log.e("firebase_regid >> ", " > " + firebase_regid);
            mobilenum_str = mobilenum.getText().toString();
            Log.e("mobilenum_str", mobilenum_str);
            password_str = password_et.getText().toString();
            if (mobilenum_str == null || mobilenum_str.equalsIgnoreCase("")) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.entermobile), Toast.LENGTH_LONG).show();
            } else if (password_str == null || password_str.equalsIgnoreCase("")) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.enterpass), Toast.LENGTH_LONG).show();
            } else {
                new LoginAsc().execute();
                //  Toast.makeText(LoginActivity.this, "sseesecc", Toast.LENGTH_SHORT).show();
            }

        });

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dontaccount.setOnClickListener(new View.OnClickListener() {
            private String Url;

            @Override
            public void onClick(View v) {

                Random addition1 = new Random();
                int additionint1 = addition1.nextInt(100) + 1;
                String random_no = String.valueOf(additionint1);
                Url = "https://myngrewards.com/signup.php?affiliate_name=OFFICIALNG&affiliate_no=" + random_no + "&how_invited_you=";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                startActivity(intent);

            }
        });


        businesslogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MarchantLogin.class);
                startActivity(i);
                finish();
            }
        });


        forgot_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MemberForgotPass.class);
                startActivity(i);
                finish();
            }
        });


        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.Status_Facebook, "true");
                PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.Logout_Status, "true");
                loginButton.performClick();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                FacebookSdk.sdkInitialize(getApplicationContext());
                FacebookSdk.setIsDebugEnabled(true);
                Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(LoginActivity.this));
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                firebase_regid = pref.getString("regId", null);
                Log.e("face firebase_regid >> ", " > " + firebase_regid);
                facebookData();

            }
        });

        login_with_touch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                fingerPrintLay();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void fingerPrintLay() {

        final Dialog dialogSts = new Dialog(LoginActivity.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.fingerprint_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        textView = (TextView) dialogSts.findViewById(R.id.errorText);
        TextView cancel_tv = (TextView) dialogSts.findViewById(R.id.cancel_tv);

// Check whether the device has a Fingerprint sensor.
        if (!fingerprintManager.isHardwareDetected()) {
            /**
             * An error message will be displayed if the device does not contain the fingerprint hardware.
             * However if you plan to implement a default authentication method,
             * you can redirect the user to a default authentication activity from here.
             * Example:
             * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
             * startActivity(intent);
             */
            textView.setText("Your Device does not have a Fingerprint Sensor");

        } else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                textView.setText("Fingerprint authentication permission not enabled");

            } else {
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    textView.setText("Register at least one fingerprint in Settings");
                } else {
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                        textView.setText("Lock screen security not enabled in Settings");
                    } else {

                        generateKey();

                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintHandler helper = new FingerprintHandler(LoginActivity.this, textView, "Member");
                            helper.startAuth(fingerprintManager, cryptoObject);
                        }
                    }
                }
            }
        }

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogSts.dismiss();
            }
        });

        dialogSts.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void idint() {
        loginButton = (LoginButton) findViewById(R.id.login_button_fb);
        facebook_button = (LinearLayout) findViewById(R.id.facebook_button);
        country_spn = findViewById(R.id.country_spn);
        login_with_touch = findViewById(R.id.login_with_touch);
        sss = findViewById(R.id.sss);
        forgot_tv = findViewById(R.id.forgot_tv);
        progresbar = findViewById(R.id.progresbar);
        password_et = findViewById(R.id.password_et);
        mobilenum = findViewById(R.id.mobilenum);
        businesslogin = findViewById(R.id.businesslogin);
        backlay = findViewById(R.id.backlay);
        bytaping = findViewById(R.id.bytaping);
        login_tv = findViewById(R.id.login_tv);
        dontaccount = findViewById(R.id.dontaccount);
        privacy_policy = (TextView) findViewById(R.id.privacy_policy);

        String txt = "To login, member can use ";
        String boltxt = "Username";
        String bolmob = "Mobile No.";
        String txtfor = " and Password to Login";
        String www = "<font color='#000000'>" + boltxt + "</font>";
        String wwww = "<font color='#000000'>" + bolmob + "</font>";
        String sourceString = "<b>" + www + "</b> ";
        String ssss = "<b>" + wwww + "</b> ";

        sss.setText(Html.fromHtml("" + txt + "" + sourceString + " or " + ssss + "" + txtfor));
        String first_hh = getResources().getString(R.string.donthavemar);
        String first = getResources().getString(R.string.bytap);
        String first_am = getResources().getString(R.string.amper);

        String signup = "<font color='#f60241'>" + getResources().getString(R.string.signup) + "</font>";
        String signupssss = "<b>" + signup + "</b> ";
        String next = "<font color='#f60241'>" + getResources().getString(R.string.termsser) + "</font>";
        String next_1 = "<font color='#f60241'>" + getResources().getString(R.string.privacy) + "</font>";

        privacy_policy.setText(Html.fromHtml(first_am + next_1));
        bytaping.setText(Html.fromHtml(first + "\n" + " " + next));

        dontaccount.setText(Html.fromHtml(first_hh + "" + signupssss));

        bytaping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, TermsAndCondition.class);
                intent.putExtra("status", "terms");
                startActivity(intent);
                finish();
            }
        });


        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, TermsAndCondition.class);
                intent.putExtra("status", "privacy");
                startActivity(intent);
                finish();
            }
        });
    }

    private class LoginAsc extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                String postReceiverUrl = BaseUrl.baseurl + "member_login.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("username", mobilenum_str);
                params.put("phone", mobilenum_str);
                params.put("password", password_str);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("country_name", country_str);
                params.put("country_id", country_id);
                params.put("device_token", firebase_regid);

                Log.e("url>>>", url + " Device Token : " + firebase_regid);

                StringBuilder postData = new StringBuilder();

                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }

                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();
                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();

                Log.e("Json Login Response", ">>>>>>>>>>>>" + response);
                return response;

            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();

            } catch (IOException e1) {

                e1.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);
            if (result == null) {

            } else if (result.isEmpty()) {

            } else {

                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(result);
                    String result_chk = jsonObject.getString("status");

                    if (result_chk.equalsIgnoreCase("1")) {

                        mySession.setlogindata(result);
                        mySession.signinusers(true);

                        if (newLogoutStatus.equals("false")) {

                            PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.Status_Facebook, "");
                        }

                        PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.Logout_Status, "false");
                        Intent i = new Intent(LoginActivity.this, MainTabActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        i.putExtra("logout_status", newLogoutStatus);
                        startActivity(i);

                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credential", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void facebookData() {

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile email");

        if (newLogoutStatus.equals("false")) {

            PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.Status_Facebook, "");

        }

        if (AccessToken.getCurrentAccessToken() != null) {

            RequestData();

            Log.e("hello >>>>>", "if");
        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    Log.e("hellosuccess", "registerCallback");
                    RequestData();
                }
            }

            @Override
            public void onCancel() {

                accessToken = AccessToken.getCurrentAccessToken();

                if (accessToken == null) {

                } else {
                    Toast.makeText(LoginActivity.this, "Login Successful. ", Toast.LENGTH_LONG).show();

                }

                finish();
            }

            @Override
            public void onError(FacebookException exception) {

                Log.e("errorfb", exception.getMessage());
                finish();

            }

        });
    }

    public void RequestData() {

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                try {

                    if (json != null) {

                        Log.e("Access facebook >>>>", "" + AccessToken.getCurrentAccessToken());
                        Log.e("json facebook >>>>", "" + json);

                        facebook_id = json.getString("id");
                        Log.e("json >>>>", "" + json);
                        if (json.has("email")) {
                            facebook_email = json.getString("email");

                        } else {
                            facebook_email = "";
                        }

                        facebook_image = "http://graph.facebook.com/" + facebook_id + "/picture?type=large&height=320&width=420";
                        Log.e("facebook_image >>", facebook_image);

                        facebook_name = json.getString("first_name") + " " + json.getString("last_name");
                        facebook_lastname = json.getString("last_name");
                        face_username = json.getString("name");

                        Log.e("face_gender >>>", "" + face_gender);
                        Log.e("facebook_name >>>", "" + facebook_name);
                        Log.e("facebook_id >>>", "" + facebook_id);
                        Log.e("facebook_image >>>", "" + facebook_image);
                        Log.e("facebook_name >>>", "" + facebook_name);
                        Log.e("facebook_id >>>", "" + facebook_id);

                        if (newLogoutStatus.equals("false")) {

                            PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.Status_Facebook, "");

                        }

                        PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.FaceBook_Name, facebook_name);
                        PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.facebook_image, facebook_image);
                        PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.FaceBook_Email, facebook_email);

                        String value_fb = "1";
                        if (facebook_name.length() > 0) {
                            new SocialLogin().execute();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,name,link,email,picture,gender,locale");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private class SocialLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);

            try {

                super.onPreExecute();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                String postReceiverUrl = BaseUrl.baseurl + "member_social_login.php?";

                URL url = new URL(postReceiverUrl);

                Log.e("social_id", facebook_id);
                Log.e("fullname", facebook_name);
                Log.e("member_image", facebook_image);
                Log.e("email", facebook_email);
                Log.e("latitude", "" + latitude);
                Log.e("device_token", firebase_regid);
                Log.e("longitude", "" + longitude);
                Log.e("country_name", "" + country_str);
                Log.e("country_id", "" + country_id);

                Map<String, Object> params = new LinkedHashMap<>();
                params.put("social_id", facebook_id);
                params.put("fullname", facebook_name);
                params.put("member_image", facebook_image);
                params.put("email", facebook_email);
                params.put("latitude", latitude);
                params.put("device_token", firebase_regid);
                params.put("longitude", longitude);
                params.put("country_name", country_str);
                params.put("country_id", country_id);

                StringBuilder postData = new StringBuilder();

                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }

                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();
                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    response += line;
                }

                writer.close();
                reader.close();

                Log.e("Json Social Response", ">>>>>>>>>>>>" + response);

                try {

                    JSONObject object = new JSONObject(response);

                    result = object.getString("result");

                    Log.e("json result", result);
                    JSONObject object1 = new JSONObject(result);

                    phone = object1.getString("phone");
                    invited_user_name = object1.getString("how_invited_you");
                    fullname = object1.getString("fullname");

                    PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.Status_Facebook, "true");
                    PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.Phone, phone);
                    PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.Full_Name, fullname);
                    PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.Invited, invited_user_name);

                } catch (JSONException e) {

                    e.printStackTrace();
                }

                return response;

            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();

            } catch (IOException e1) {

                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);

            if (result == null) {

            } else if (result.isEmpty()) {

            } else {

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("1")) {

                        mySession.setlogindata(result);
                        mySession.signinusers(true);

                        Intent i = new Intent(LoginActivity.this, MainTabActivity.class);
                        i.putExtra("facebook_name", facebook_name);
                        i.putExtra("face_gender", face_gender);
                        i.putExtra("facebook_image", facebook_image);
                        i.putExtra("logout_status", newLogoutStatus);

                        Log.e("face_gender >>>", "" + face_gender);
                        Log.e("facebook_name >>>", "" + facebook_name);
                        Log.e("facebook_id >>>", "" + facebook_id);
                        Log.e("facebook_image >>>", "" + facebook_image);
                        Log.e("facebook_name >>>", "" + facebook_name);
                        Log.e("facebook_id >>>", "" + facebook_id);

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        startActivity(i);

                    } else {

                        Toast.makeText(LoginActivity.this, "Invalid Username and password", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public class CountryListAdapter extends BaseAdapter {
        Context context;

        LayoutInflater inflter;
        private final ArrayList<CountryBean> values;

        public CountryListAdapter(Context applicationContext, ArrayList<CountryBean> values) {
            this.context = applicationContext;
            this.values = values;

            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {

            return values == null ? 0 : values.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.country_item_lay_flag, null);

            TextView names = (TextView) view.findViewById(R.id.name_tv);
            ImageView country_flag = (ImageView) view.findViewById(R.id.country_flag);
            if (values.get(i).getFlag_url() == null || values.get(i).getFlag_url().equalsIgnoreCase("")) {

            } else {
                Glide.with(LoginActivity.this)
                        .load(values.get(i).getFlag_url())
                        .thumbnail(0.5f)
                        .override(50, 50)
                        .centerCrop()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;

                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                return false;
                            }
                        })
                        .into(country_flag);
            }
            names.setText(values.get(i).getName());


            return view;
        }
    }

    private class GetCountryList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            countryBeanArrayList = new ArrayList<>();
            countryBeanArrayList.clear();
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "countries.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();
                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();
                Log.e("Json Country Response", ">>>>>>>>>>>>" + response);
                return response;
            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();
            } catch (IOException e1) {

                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);
            if (result == null) {

            } else if (result.isEmpty()) {

            } else {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("successful")) {
                        myapisession.setCountrydata(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CountryBean countryBean = new CountryBean();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            countryBean.setId(jsonObject1.getString("id"));
                            countryBean.setName(jsonObject1.getString("name"));
                            countryBean.setSortname(jsonObject1.getString("sortname"));
                            countryBean.setFlag_url(jsonObject1.getString("flag"));
                            countryBeanArrayList.add(countryBean);
                        }

                        if (countryBeanArrayList != null) {
                            Collections.reverse(countryBeanArrayList);
                        }

                        countryListAdapter = new CountryListAdapter(LoginActivity.this, countryBeanArrayList);
                        country_spn.setAdapter(countryListAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void checkGps() {
        gpsTracker = new GPSTracker(LoginActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            if (latitude == 0.0) {
                latitude = SplashActivity.latitude;
                longitude = SplashActivity.longitude;

            }
        } else {

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            } else {
                latitude = SplashActivity.latitude;
                longitude = SplashActivity.longitude;
                Log.e("LAT", "" + latitude);
                Log.e("LON", "" + longitude);

            }
        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}