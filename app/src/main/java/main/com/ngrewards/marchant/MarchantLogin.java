package main.com.ngrewards.marchant;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.FingerprintHandler;
import main.com.ngrewards.activity.SplashActivity;
import main.com.ngrewards.activity.TermsAndCondition;
import main.com.ngrewards.activity.app.Config;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.CountryBean;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.marchant.activity.ForgotPass;
import main.com.ngrewards.marchant.activity.MerchantSignupSlider;
import main.com.ngrewards.marchant.merchantbottum.MerchantBottumAct;

public class MarchantLogin extends AppCompatActivity {
    //code for lat long
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    public static String country_str = "", country_id = "";
    MySession mySession;
    Myapisession myapisession;
    CountryListAdapter countryListAdapter;
    String facebook_name, facebook_email, facebook_id, facebook_image, face_gender, face_locale, facebook_lastname = "", face_username;
    LocationManager locationManager;
    Location location;
    GPSTracker gpsTracker;
    KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    String KEY_NAME = "androidHive";
    Cipher cipher;
    TextView textView;
    private RelativeLayout backlay;
    private TextView bytaping, dontaccount, login_tv, marforgot, login_with_touch;
    private EditText mobilenum, password_et;
    private String mobilenum_str = "", password_str = "", firebase_regid = "";
    private ProgressBar progresbar;
    private Spinner country_spn;
    //private LoginButton loginButton;
    private LinearLayout facebook_button;
    //private CallbackManager callbackManager;
    private ArrayList<CountryBean> countryBeanArrayList;
    private double latitude = 0, longitude = 0;
    private TextView privacy_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    FacebookSdk.sdkInitialize(getApplicationContext());
        // AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_marchant_login);
        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        //  callbackManager = CallbackManager.Factory.create();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MarchantLogin.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MarchantLogin.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        checkGps();
        idint();

        clickevet();

        //  if (myapisession.getKeyCountry() == null || myapisession.getKeyCountry().equalsIgnoreCase("")) {
        new GetCountryList().execute();
//        } else {
//            JSONObject jsonObject = null;
//            try {
//                countryBeanArrayList = new ArrayList<>();
//                jsonObject = new JSONObject(myapisession.getKeyCountry());
//                String message = jsonObject.getString("message");
//                if (message.equalsIgnoreCase("successful")) {
//                    JSONArray jsonArray = jsonObject.getJSONArray("result");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        CountryBean countryBean = new CountryBean();
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        countryBean.setId(jsonObject1.getString("id"));
//                        countryBean.setName(jsonObject1.getString("name"));
//                        countryBean.setSortname(jsonObject1.getString("sortname"));
//                        countryBean.setFlag_url(jsonObject1.getString("flag"));
//                        countryBeanArrayList.add(countryBean);
//                    }
//                    if (countryBeanArrayList != null) {
//                        Collections.reverse(countryBeanArrayList);
//                    }
//
//                    FacebookSdk.sdkInitialize(getApplicationContext());
//                    Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(this));
//
//                   /* countryListAdapter = new CountryListAdapter(LoginActivity.this, android.R.layout.simple_spinner_item, countryBeanArrayList);
//                    country_spn.setAdapter(countryListAdapter);*/
//                    countryListAdapter = new CountryListAdapter(MarchantLogin.this, countryBeanArrayList);
//                    country_spn.setAdapter(countryListAdapter);
//                    countryListAdapter.notifyDataSetChanged();
//                } else {
//                    new GetCountryList().execute();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
    }

    private void clickevet() {
        login_with_touch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                fingerPrintLay();
            }
        });

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

        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                firebase_regid = pref.getString("regId", null);
                mobilenum_str = mobilenum.getText().toString();
                password_str = password_et.getText().toString();
                if (mobilenum_str == null || mobilenum_str.equalsIgnoreCase("")) {
                    Toast.makeText(MarchantLogin.this, getResources().getString(R.string.entermerchantno), Toast.LENGTH_LONG).show();
                } else if (password_str == null || password_str.equalsIgnoreCase("")) {
                    Toast.makeText(MarchantLogin.this, getResources().getString(R.string.enterpass), Toast.LENGTH_LONG).show();

                } else {
                    new LoginAsc().execute();
                }
            }
        });
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dontaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MarchantLogin.this, MerchantSignupSlider.class);
                startActivity(i);
                finish();
            }
        });

        marforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MarchantLogin.this, ForgotPass.class);
                startActivity(i);

            }
        });

        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // loginButton.performClick();
            }
        });
/*
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                firebase_regid = pref.getString("regId", null);
                facebookData();
            }
        });
*/
       /* countryListAdapter = new CountryListAdapter(MarchantLogin.this, StartSliderAct.countryBeanArrayList);
        country_spn.setAdapter(countryListAdapter);*/
        /*countryListAdapter = new CountryListAdapter(MarchantLogin.this, android.R.layout.simple_spinner_item, StartSliderAct.countryBeanArrayList);
        country_spn.setAdapter(countryListAdapter);*/
    }

    private void idint() {

        //    loginButton = (LoginButton) findViewById(R.id.login_button_fb);
        facebook_button = (LinearLayout) findViewById(R.id.facebook_button);
        country_spn = findViewById(R.id.country_spn);
        login_with_touch = findViewById(R.id.login_with_touch);
        marforgot = findViewById(R.id.marforgot);
        progresbar = findViewById(R.id.progresbar);
        password_et = findViewById(R.id.password_et);
        mobilenum = findViewById(R.id.mobilenum);
        backlay = findViewById(R.id.backlay);
        bytaping = findViewById(R.id.bytaping);
        login_tv = findViewById(R.id.login_tv);
        dontaccount = findViewById(R.id.dontaccount);
        privacy_policy = findViewById(R.id.privacy_policy);

        String first_hh = getResources().getString(R.string.donthavemarchant);
        String first = getResources().getString(R.string.bytap);
        String first_am = getResources().getString(R.string.amper);
        String signup = "<font color='#f60241'>" + getResources().getString(R.string.signup) + "</font>";
        String next = "<font color='#f60241'>" + getResources().getString(R.string.termsser) + "</font>";
        String next_1 = "<font color='#f60241'>" + getResources().getString(R.string.privacy) + "</font>";
        privacy_policy.setText(Html.fromHtml(first_am + next_1));
        bytaping.setText(Html.fromHtml(first + "\n" + " " + next));
        dontaccount.setText(Html.fromHtml(first_hh + " " + signup));

        bytaping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MarchantLogin.this, TermsAndCondition.class);
                intent.putExtra("status", "terms");
                startActivity(intent);
                finish();
            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MarchantLogin.this, TermsAndCondition.class);
                intent.putExtra("status", "privacy");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void checkGps() {
        gpsTracker = new GPSTracker(MarchantLogin.this);
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

/*    private void facebookData() {
        Log.e("hello >>>>>", "call method");

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile email");

        if (AccessToken.getCurrentAccessToken() != null) {
            RequestData();
            Log.e("hello >>>>>", "if");
        }


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    Log.e("hello >>>>>", "registerCallback");
                    RequestData();
                }
            }

            @Override
            public void onCancel() {
                Log.e("helll", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {

                Log.e("helll", "error>>" + exception.getMessage());

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
                        //  face_gender = json.getString("gender");

                        Log.e("facebook_email >>>", "" + facebook_email);
                        Log.e("facebook_name >>>", "" + facebook_name);
                        Log.e("facebook_id >>>", "" + facebook_id);
                        Log.e("facebook_image >>>", "" + facebook_image);
                        Log.e("facebook_name >>>", "" + facebook_name);
                        Log.e("facebook_id >>>", "" + facebook_id);


                        String value_fb = "1";
                        if (facebook_name.length() > 0) {

                            new SocialLogin().execute();
                            // new SocialLogin2().execute();
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
    }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fingerPrintLay() {
        final Dialog dialogSts = new Dialog(MarchantLogin.this, R.style.DialogSlideAnim);
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
            textView.setText(getString(R.string.your_device_does_not_have_a_fingerprint_sensor));
        } else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                textView.setText(getString(R.string.fingerprint_authentication_permission_not_enabled));
            } else {
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    textView.setText(getString(R.string.register_at_least_one_fingerprint_in_settings));
                } else {
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                        textView.setText(getString(R.string.lock_screen_security_not_enabled_in_settings));
                    } else {
                        generateKey();


                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintHandler helper = new FingerprintHandler(MarchantLogin.this, textView, "Merchant");
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
        } catch (NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException
                 | CertificateException | IOException e) {
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

    public static class CountryListAdapter extends BaseAdapter {
        private final ArrayList<CountryBean> values;
        Context context;
        LayoutInflater inflter;

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
            //  TextView countryname = (TextView) view.findViewById(R.id.countryname);
            if (values.get(i).getFlag_url() == null || values.get(i).getFlag_url().equalsIgnoreCase("")) {

            } else {
                Glide.with(context)
                        .load(values.get(i).getFlag_url())
                        .thumbnail(0.5f)
                        .override(50, 50)
                        .centerCrop()

                        .diskCacheStrategy(DiskCacheStrategy.ALL)

                        .into(country_flag);
            }


            names.setText(values.get(i).getName());


            return view;
        }
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
                String postReceiverUrl = BaseUrl.baseurl + "merchant_login.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("business_no", mobilenum_str);
                params.put("password", password_str);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("country", country_str);
                params.put("country_id", country_id);
                params.put("device_token", firebase_regid);
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
                        Log.e("", "jsonObjectjsonObjectjsonObjectjsonObject:------ " + result);

                        mySession.setlogindata(result);
                        mySession.signinusers(true);

                        Intent i = new Intent(MarchantLogin.this, MerchantBottumAct.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    } else {
                        Toast.makeText(MarchantLogin.this, "Invalid Credential", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
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
                String postReceiverUrl = BaseUrl.baseurl + "merchant_social_login.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                Log.e("country_str >>", " > " + country_str);
                Log.e("country_id >>", " > " + country_id);
                params.put("social_id", facebook_id);
                params.put("fullname", facebook_name);
                params.put("merchant_image", facebook_image);
                params.put("email", facebook_email);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("country", country_str);
                params.put("country_id", country_id);
                params.put("device_token", firebase_regid);
                params.put("day_name", "Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday");
                params.put("opening_time", "0:0,0:0,0:0,0:0,0:0,0:0,0:0");
                params.put("closing_time", "0:0,0:0,0:0,0:0,0:0,0:0,0:0");
                params.put("opening_status", "OPEN,OPEN,OPEN,OPEN,OPEN,OPEN,OPEN");

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
                        Log.e("", "jsonObjectjsonObjectjsonObjectjsonObject:------ " + result);
                        mySession.setlogindata(result);
                        mySession.signinusers(true);

                        Intent i = new Intent(MarchantLogin.this, MerchantBottumAct.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);


                    } else {
                        Toast.makeText(MarchantLogin.this, getString(R.string.invalid_username_and_password), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    private class GetCountryList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            countryBeanArrayList = new ArrayList<>();
            countryBeanArrayList.clear();
        /*    CountryBean countryListBean = new CountryBean();
            countryListBean.setId("0");
            countryListBean.setName("Country");
            countryListBean.setSortname("");
            countryBeanArrayList.add(countryListBean);*/
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


                   /* countryListAdapter = new CountryListAdapter(LoginActivity.this, android.R.layout.simple_spinner_item, countryBeanArrayList);
                    country_spn.setAdapter(countryListAdapter);*/
                        countryListAdapter = new CountryListAdapter(MarchantLogin.this, countryBeanArrayList);
                        country_spn.setAdapter(countryListAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
}