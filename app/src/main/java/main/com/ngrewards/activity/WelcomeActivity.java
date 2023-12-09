package main.com.ngrewards.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.com.ngrewards.R;
import main.com.ngrewards.androidmigx.MainTabActivity;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MySession;

public class WelcomeActivity extends AppCompatActivity {
    //code for lat long
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    public static String member_email_str = "";
    private final String country_str = "";
    private final String country_id = "";
    LocationManager locationManager;
    Location location;
    GPSTracker gpsTracker;
    String facebook_name, facebook_email, facebook_id, facebook_image, face_gender, face_locale, facebook_lastname = "", face_username;
    private TextView termscheck;
    private CheckBox termscheck1;
    private TextView allreadyaccount, createaccount;
    private EditText member_email;
    private ProgressBar progresbar;
    private double latitude = 0, longitude = 0;
    // private LoginButton loginButton;
    private LinearLayout facebook_button;
    // private CallbackManager callbackManager;
    private MySession mySession;
    private final String firebase_regid = "";
    private RelativeLayout backlay;

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_welcome);

        //  callbackManager = CallbackManager.Factory.create();
        mySession = new MySession(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(WelcomeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WelcomeActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        idinit();
        clickevent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void clickevent() {

        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //    loginButton.performClick();
            }
        });

/*
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                firebase_regid = pref.getString("regId", null);
                Log.e("face firebase_regid >> ", " > " + firebase_regid);
                facebookData();
            }
        });
*/

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member_email_str = member_email.getText().toString();

                if (!isEmailValid(member_email_str)) {
                    Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.entervalidemailaddress), Toast.LENGTH_LONG).show();
                } else if (member_email_str == null || member_email_str.equalsIgnoreCase("")) {
                    Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.enteryemail), Toast.LENGTH_LONG).show();
                } else {
                    if (termscheck1.isChecked()) {

                        new CheckUsernameAvability().execute();

                    } else {
                        Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.youneedaccept), Toast.LENGTH_LONG).show();
                    }


                }

            }
        });

        allreadyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idinit() {

        backlay = findViewById(R.id.backlay);

        termscheck1 = findViewById(R.id.termscheck1);


        //  loginButton = (LoginButton) findViewById(R.id.login_button_fb);
        facebook_button = (LinearLayout) findViewById(R.id.facebook_button);
        progresbar = findViewById(R.id.progresbar);
        member_email = findViewById(R.id.member_email);
        allreadyaccount = findViewById(R.id.allreadyaccount);
        termscheck = findViewById(R.id.termscheck);
        createaccount = findViewById(R.id.createaccount);
        String first = getResources().getString(R.string.byreg);
        String next = "<font color='#f60241'>" + getResources().getString(R.string.termsser) + "</font>";
        termscheck.setText(Html.fromHtml(first + " " + next));
        String allready = getResources().getString(R.string.allready);
        String allready_f = "<font color='#000000'>" + getResources().getString(R.string.loginhere) + "</font>";
        allreadyaccount.setText(Html.fromHtml(allready + " " + allready_f));

        termscheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WelcomeActivity.this, TermsAndCondition.class);
                intent.putExtra("status", "terms");
                startActivity(intent);
            }
        });


    }

    private void facebookData() {

        Log.e("hello >>>>>", "call method");

      /*  callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile email");

        if (AccessToken.getCurrentAccessToken() != null) {
            RequestData();
            Log.e("hello >>>>>", "if");
        }*/


/*
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
*/
    }

//    public void RequestData() {
//        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//                JSONObject json = response.getJSONObject();
//                try {
//                    if (json != null) {
//                        Log.e("Access facebook >>>>", "" + AccessToken.getCurrentAccessToken());
//                        Log.e("json facebook >>>>", "" + json);
//
//                        facebook_id = json.getString("id");
//                        Log.e("json >>>>", "" + json);
//                        if (json.has("email")) {
//                            facebook_email = json.getString("email");
//                        } else {
//                            facebook_email = "";
//                        }
//
//                        facebook_image = "http://graph.facebook.com/" + facebook_id + "/picture?type=large&height=320&width=420";
//                        Log.e("facebook_image >>", facebook_image);
//
//                        facebook_name = json.getString("first_name") + " " + json.getString("last_name");
//                        facebook_lastname = json.getString("last_name");
//                        face_username = json.getString("name");
//                        // face_gender = json.getString("gender");
//
//                        Log.e("face_gender >>>", "" + face_gender);
//                        Log.e("facebook_name >>>", "" + facebook_name);
//                        Log.e("facebook_id >>>", "" + facebook_id);
//                        Log.e("facebook_image >>>", "" + facebook_image);
//                        Log.e("facebook_name >>>", "" + facebook_name);
//                        Log.e("facebook_id >>>", "" + facebook_id);
//
//
//                        String value_fb = "1";
//                        if (facebook_name.length() > 0) {
//
//                            new SocialLogin().execute();
//                            // new SocialLogin2().execute();
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,first_name,last_name,name,link,email,picture,gender,locale");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }

    private class CheckUsernameAvability extends AsyncTask<String, String, String> {
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
                String postReceiverUrl = BaseUrl.baseurl + "check_member.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("email", member_email_str);
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
                        Intent i = new Intent(WelcomeActivity.this, SliderActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(WelcomeActivity.this, "This email address is not available", Toast.LENGTH_LONG).show();
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

                String postReceiverUrl = BaseUrl.baseurl + "member_social_login.php?";
                URL url = new URL(postReceiverUrl);
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

                        Intent i = new Intent(WelcomeActivity.this, MainTabActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);


                    } else {

                        Toast.makeText(WelcomeActivity.this, "Invalid Username and password", Toast.LENGTH_LONG).show();

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
