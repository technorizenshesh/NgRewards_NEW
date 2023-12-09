package main.com.ngrewards.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.app.Config;
import main.com.ngrewards.androidmigx.MainTabActivity;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.CustomViewPager;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MultipartUtility;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.fragments.EnterEmailFrag;
import main.com.ngrewards.fragments.EnterMobfrag;
import main.com.ngrewards.fragments.EnterPassFrag;
import main.com.ngrewards.fragments.LetsPicUserFrag;
import main.com.ngrewards.fragments.UploadImageNameFrag;
import main.com.ngrewards.fragments.WhoInvitedFrag;

public class SliderActivity extends AppCompatActivity {

    //code for lat long
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    public static String member_email_str = "", member_pass_str = "", member_img_path = "", member_first_last = "", user_name_str = "", user_number_str = "",
            who_invite_id = "";
    public static Bitmap member_bitmap;
    ViewPagerAdapter adapter;
    MySession mySession;
    LocationManager locationManager;
    Location location;
    GPSTracker gpsTracker;
    private Button continue_button;
    private CustomViewPager viewPager;
    private CirclePageIndicator indicator;
    private RelativeLayout backlay;
    private boolean click_sts = false;
    private ProgressBar progresbar;
    private String firebase_regid = "";
    private double latitude = 0, longitude = 0;
    private String time_zone = "";
    private String Who_invited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        time_zone = tz.getID();
        viewPager = findViewById(R.id.viewpager);
        mySession = new MySession(this);
        viewPager.setPagingEnabled(false);
        setupViewPager(viewPager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicatortwo);
        continue_button = (Button) findViewById(R.id.continue_button);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(SliderActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SliderActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        checkGps();
        idint();
        clicklistener();
    }

    private void idint() {
        backlay = findViewById(R.id.backlay);
        progresbar = findViewById(R.id.progresbar);
    }

    private void clicklistener() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0) {
                    finish();
                } else {
                    if (adapter == null) {
                    } else {
                        if (viewPager.getCurrentItem() > 0) {
                            click_sts = false;
                            continue_button.setText(getResources().getString(R.string.next));
                            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

                        }
                    }
                }

            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                firebase_regid = pref.getString("regId", null);
                if (adapter == null) {
                } else {
                    if (viewPager.getCurrentItem() == 4) {
                        click_sts = true;
                        continue_button.setText(getResources().getString(R.string.finish));
                    }

                    if (viewPager.getCurrentItem() <= 5) {
                        if (viewPager.getCurrentItem() == 0) {
                            if (WelcomeActivity.member_email_str != null && !WelcomeActivity.member_email_str.equalsIgnoreCase("")) {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            } else {
                                Toast.makeText(SliderActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();
                            }
                        } else if (viewPager.getCurrentItem() == 1) {
                            if (member_pass_str != null && !member_pass_str.equalsIgnoreCase("")) {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            } else {
                                Toast.makeText(SliderActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();
                            }
                        } else if (viewPager.getCurrentItem() == 2) {
                            if (user_number_str != null && !user_number_str.equalsIgnoreCase("")) {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

                            } else {
                                Toast.makeText(SliderActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();
                            }

                        } else if (viewPager.getCurrentItem() == 3) {
                            if (member_first_last != null && !member_first_last.equalsIgnoreCase("")) {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            } else {
                                Toast.makeText(SliderActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();
                            }
                        } else if (viewPager.getCurrentItem() == 4) {
                            if (user_name_str != null && !user_name_str.equalsIgnoreCase("")) {
                                new CheckUsernameAvability().execute();

                            } else {
                                Toast.makeText(SliderActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();
                            }
                        } else if (viewPager.getCurrentItem() == 5) {

                            new SignupMembers().execute();
                        }
                    }
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                indicator.setViewPager(viewPager);
                final float density = getResources().getDisplayMetrics().density;
                indicator.setRadius(5 * density);
                if (i == 4) {
                    click_sts = true;
                    continue_button.setText(getResources().getString(R.string.finish));

                }

                Log.e("hello", "first >" + i);
            }

            @Override
            public void onPageSelected(int i) {
                Log.e("hello", "second>" + i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.e("hello", "third>" + i);

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EnterEmailFrag(), "ONE");
        adapter.addFragment(new EnterPassFrag(), "TWO");
        adapter.addFragment(new EnterMobfrag(), "TWO");
        adapter.addFragment(new UploadImageNameFrag(), "ONE");
        adapter.addFragment(new LetsPicUserFrag(), "TWO");
        adapter.addFragment(new WhoInvitedFrag(), "TWO");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void checkGps() {
        gpsTracker = new GPSTracker(SliderActivity.this);
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public class SignupMembers extends AsyncTask<String, String, String> {
        String Jsondata;
        private boolean checkdata = false;

        protected void onPreExecute() {
            try {
                super.onPreExecute();
                progresbar.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                checkdata = true;
            }
        }

        @Override
        protected String doInBackground(String... strings) {


            String charset = "UTF-8";
            String requestURL = BaseUrl.baseurl + "signup_member.php?";
            String requestURLs = BaseUrl.baseurl + "signup_member.php?email=" + WelcomeActivity.member_email_str + "&password=" + member_pass_str + "&phone=" + user_number_str + "&affiliate_name=" + user_name_str +
                    "&how_invited_you=" + WhoInvitedFrag.who_invite_id + "&country_name=" + LoginActivity.country_str + "&country_id=" + LoginActivity.country_id + "&fullname=" + member_first_last + "&latitude=" + latitude + "&longitude=" + longitude + "&device_token=" + firebase_regid + "&timezone=" + time_zone;
            Log.e("requestURL >>", requestURL);
            Log.e("requestURLs >>", requestURLs);
            Log.e("time_zone >>", time_zone);

            try {

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("email", WelcomeActivity.member_email_str);
                multipart.addFormField("password", member_pass_str);
                multipart.addFormField("phone", user_number_str);
                multipart.addFormField("timezone", time_zone.trim());
                multipart.addFormField("affiliate_name", user_name_str);
                multipart.addFormField("how_invited_you", WhoInvitedFrag.who_invite_id);
                multipart.addFormField("country_name", LoginActivity.country_str);
                multipart.addFormField("country_id", LoginActivity.country_id);
                multipart.addFormField("fullname", member_first_last);
                multipart.addFormField("latitude", "" + latitude);
                multipart.addFormField("longitude", "" + longitude);
                multipart.addFormField("device_token", firebase_regid);

                if (member_img_path == null || member_img_path.equalsIgnoreCase("")) {

                } else {

                    File ImageFile = new File(member_img_path);
                    multipart.addFilePart("member_image", ImageFile);
                }

                List<String> response = multipart.finish();

                for (String line : response) {

                    Jsondata = line;
                    Log.e("Signup Member Res==", Jsondata);

                }
                JSONObject object = new JSONObject(Jsondata);
                return Jsondata;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
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
                        Intent i = new Intent(SliderActivity.this, MainTabActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    } else {
                        Toast.makeText(SliderActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }

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
                params.put("user_name", user_name_str);
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
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

                    } else {
                        Toast.makeText(SliderActivity.this, "This username is not available", Toast.LENGTH_LONG).show();
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
