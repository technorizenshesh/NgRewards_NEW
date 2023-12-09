package main.com.ngrewards.marchant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.SplashActivity;
import main.com.ngrewards.activity.app.Config;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.CustomViewPager;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MultipartUtility;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.marchant.fragments.MerBusinessFrag;
import main.com.ngrewards.marchant.fragments.MerEnterEmailFrag;
import main.com.ngrewards.marchant.fragments.MerEnterPassFrag;
import main.com.ngrewards.marchant.fragments.MerGiveNgReward;
import main.com.ngrewards.marchant.fragments.MerUploadImageNameFrag;
import main.com.ngrewards.marchant.fragments.MerWhoInvitedFrag;
import main.com.ngrewards.marchant.fragments.WelcomeFragMerchant;
import main.com.ngrewards.marchant.merchantbottum.MerchantBottumAct;

public class MerchantSignupSlider extends AppCompatActivity {

    //code for lat long
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    public static String add_merchant_in_member = "NO";
    public static String ImagePath = "", bus_category_id = "", selected_country = "", selected_country_name = "", mer_address_two = "", country_str = "", mer_email = "", mer_pass = "", mer_tnc_cheched = "", mer_fullname = "", mer_reward = "6", mer_who_invite = "", mer_image = "", mer_businessname = "", mer_phone_number = "", mer_address = "", mer_city = "", mer_state = "", mer_zipcode = "";
    public static double mer_latitude = 0, mer_longitude = 0;
    private final boolean back_click_sts = false;
    String selectedItem;
    ViewPagerAdapter adapter;
    MySession mySession;
    LocationManager locationManager;
    Location location;
    GPSTracker gpsTracker;
    private Button continue_button;
    private CustomViewPager viewPager;
    private CirclePageIndicator indicator;
    private boolean click_sts = false;
    private RelativeLayout backlay;
    private ProgressBar progresbar;
    private double latitude = 0, longitude = 0;
    private String firebase_regid = "", time_zone = "";

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_merchant_signup_slider);
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        time_zone = tz.getID();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MerchantSignupSlider.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MerchantSignupSlider.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        checkGps();

        viewPager = findViewById(R.id.viewpager);
        mySession = new MySession(this);
        viewPager.setPagingEnabled(false);
        setupViewPager(viewPager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicatortwo);
        continue_button = (Button) findViewById(R.id.continue_button);
        idint();
        clicklistener();
    }

    private void idint() {
        backlay = findViewById(R.id.backlay);
        progresbar = findViewById(R.id.progresbar);
    }

    private void clicklistener() {
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                firebase_regid = pref.getString("regId", null);
                /*if (click_sts) {
                    Intent i = new Intent(MerchantSignupSlider.this, MerchantBottumAct.class);
                    startActivity(i);
                } else {*/
                if (adapter == null) {

                } else {
                    if (viewPager.getCurrentItem() == 5) {
                        click_sts = true;
                        continue_button.setText(getResources().getString(R.string.finish));
                    }
                    if (viewPager.getCurrentItem() <= 6) {
                        if (viewPager.getCurrentItem() == 0) {
                            if (!isEmailValid(mer_email)) {
                                Toast.makeText(MerchantSignupSlider.this, getResources().getString(R.string.entervalidemailaddress), Toast.LENGTH_LONG).show();

                            }

                            if (mer_email == null || mer_email.equalsIgnoreCase("")) {
                                Toast.makeText(MerchantSignupSlider.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();

                                // new CheckUsernameAvability().execute(mer_email);
                            } else {
                                CheckBox checkBox = MerchantSignupSlider.this.findViewById(R.id.termscheck);
                                if (checkBox.isChecked()) {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                } else {
                                    Toast.makeText(MerchantSignupSlider.this, getResources().getString(R.string.youneedaccept), Toast.LENGTH_LONG).show();

                                }

                            }
                        } else if (viewPager.getCurrentItem() == 1) {
                            if (mer_email != null && !mer_email.equalsIgnoreCase("")) {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            } else {
                                Toast.makeText(MerchantSignupSlider.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();
                            }
                        } else if (viewPager.getCurrentItem() == 2) {
                            if (mer_pass != null && !mer_pass.equalsIgnoreCase("")) {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            } else {
                                Toast.makeText(MerchantSignupSlider.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();
                            }
                        } else if (viewPager.getCurrentItem() == 3) {
                            if (mer_fullname != null && !mer_fullname.equalsIgnoreCase("")) {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            } else {
                                Toast.makeText(MerchantSignupSlider.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();
                            }
                        } else if (viewPager.getCurrentItem() == 4) {
                            // if (mer_businessname != null && !mer_businessname.equalsIgnoreCase("") && mer_phone_number != null && !mer_phone_number.equalsIgnoreCase("") && mer_address != null && !mer_address.equalsIgnoreCase("") && mer_city != null && !mer_city.equalsIgnoreCase("") && mer_state != null && !mer_state.equalsIgnoreCase("") && mer_zipcode != null && !mer_zipcode.equalsIgnoreCase("")) {
                            if (mer_businessname != null && !mer_businessname.equalsIgnoreCase("") &&
                                    mer_phone_number != null && !mer_phone_number.equalsIgnoreCase("")
                                    && bus_category_id != null && selected_country != null
                                    && !selected_country.equalsIgnoreCase("") &&
                                    !bus_category_id.equalsIgnoreCase("") &&
                                    !selected_country.equalsIgnoreCase("0") &&
                                    !bus_category_id.equalsIgnoreCase("0")
                                    && mer_address != null &&
                                    mer_zipcode != null && !mer_zipcode.equalsIgnoreCase("")) {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

                            } else {
                                Log.e("TAG", "onClick: mer_businessname  ---   " + mer_businessname);
                                Log.e("TAG", "onClick: mer_phone_number  ---   " + mer_phone_number);
                                Log.e("TAG", "onClick: bus_category_id   ---   " + bus_category_id);
                                Log.e("TAG", "onClick: selected_country  ---   " + selected_country);
                                Log.e("TAG", "onClick: mer_address       ---   " + mer_address);
                                Log.e("TAG", "onClick: mer_zipcode       ---   " + mer_zipcode);
                                Toast.makeText(MerchantSignupSlider.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();
                            }
                        } else if (viewPager.getCurrentItem() == 5) {
                            if (mer_reward != null && !mer_reward.equalsIgnoreCase("")) {


                                if (!mer_tnc_cheched.equalsIgnoreCase("")) {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                } else {
                                    Toast.makeText(MerchantSignupSlider.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();

                                }
                            } else {
                                Toast.makeText(MerchantSignupSlider.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();
                            }
                        } else if (viewPager.getCurrentItem() == 6) {
                            new SignupMerchant().execute();

                        }
                    }
                }
            }
        });

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

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                indicator.setViewPager(viewPager);
                final float density = getResources().getDisplayMetrics().density;
                indicator.setRadius(5 * density);
                if (i == 5) {
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
        adapter.addFragment(new WelcomeFragMerchant(), "ONE");
        adapter.addFragment(new MerEnterEmailFrag(), "ONE");
        adapter.addFragment(new MerEnterPassFrag(), "ONE");
        adapter.addFragment(new MerUploadImageNameFrag(), "ONE");
        adapter.addFragment(new MerBusinessFrag(), "TWO");
        adapter.addFragment(new MerGiveNgReward(), "ONE");
        adapter.addFragment(new MerWhoInvitedFrag(), "TWO");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void checkGps() {
        gpsTracker = new GPSTracker(MerchantSignupSlider.this);
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

    public class SignupMerchant extends AsyncTask<String, String, String> {

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
            String requestURL = BaseUrl.baseurl + "signup_merchant.php?";
            Log.e("requestURL >>", requestURL);

            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("email", mer_email);
                multipart.addFormField("add_merchant", add_merchant_in_member);
                multipart.addFormField("contact_number", mer_phone_number);
                multipart.addFormField("password", mer_pass);
                multipart.addFormField("business_name", mer_businessname);
                multipart.addFormField("contact_name", mer_fullname);
                multipart.addFormField("account_no", "");
                if (mer_latitude == 0) {
                    multipart.addFormField("latitude", "" + latitude);
                    multipart.addFormField("longitude", "" + longitude);
                } else {
                    multipart.addFormField("latitude", "" + mer_latitude);
                    multipart.addFormField("longitude", "" + mer_longitude);
                }

                multipart.addFormField("business_category", "" + bus_category_id);
                multipart.addFormField("zip_code", mer_zipcode);
                multipart.addFormField("address", mer_address);
                multipart.addFormField("address_two", mer_address_two);
                multipart.addFormField("country", selected_country_name);
                multipart.addFormField("country_id", selected_country);
                multipart.addFormField("state", mer_state);
                multipart.addFormField("city", mer_city);
                multipart.addFormField("device_token", firebase_regid);
                multipart.addFormField("reward_percent", mer_reward);
                multipart.addFormField("how_invite_you", MerchantSignupSlider.mer_who_invite);
                multipart.addFormField("sales_agent_key", "1");
                multipart.addFormField("sales_agent_no", "1");
                multipart.addFormField("sales_agent_name", "1");
                multipart.addFormField("website_url", "");
                multipart.addFormField("facebook_url", "");
                multipart.addFormField("twitter_url", "");
                multipart.addFormField("linkdin_url", "");
                multipart.addFormField("google_plus_url", "");
                multipart.addFormField("instagram_url", "");
                multipart.addFormField("business_category", "");
                multipart.addFormField("timezone", time_zone);
                multipart.addFormField("day_name", "Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday");
                multipart.addFormField("opening_time", "0:0,0:0,0:0,0:0,0:0,0:0,0:0");
                multipart.addFormField("closing_time", "0:0,0:0,0:0,0:0,0:0,0:0,0:0");
                multipart.addFormField("opening_status", "OPEN,OPEN,OPEN,OPEN,OPEN,OPEN,OPEN");
                multipart.addFormField("fullday_open_status", "DEFAULT,DEFAULT,DEFAULT,DEFAULT,DEFAULT,DEFAULT,DEFAULT");
                if (ImagePath == null || ImagePath.equalsIgnoreCase("")) {
                } else {
                    File ImageFile = new File(ImagePath);
                    multipart.addFilePart("merchant_image", ImageFile);
                }
                List<String> response = multipart.finish();
                for (String line : response) {
                    Jsondata = line;
                }
                Log.e("SIGNUParems ", " >> " + "business_category" + bus_category_id);
                Log.e("SIGNUParems ", " >> " + "zip_code" + mer_zipcode);
                Log.e("SIGNUParems ", " >> " + "address" + mer_address);
                Log.e("SIGNUParems ", " >> " + "address_two" + mer_address_two);
                Log.e("SIGNUParems ", " >> " + "country" + selected_country_name);
                Log.e("SIGNUParems ", " >> " + "country_id" + selected_country);
                Log.e("SIGNUParems ", " >> " + "state" + mer_state);
                Log.e("SIGNUParems ", " >> " + "city" + mer_city);
                Log.e("SIGNUParems ", " >> " + "device_token" + firebase_regid);
                Log.e("SIGNUParems ", " >> " + "reward_percent" + mer_reward);
                Log.e("SIGNUParems ", " >> " + "how_invite_you" + MerchantSignupSlider.mer_who_invite);
                Log.e("SIGNUParems ", " >> " + "sales_agent_key" + "1");
                Log.e("SIGNUParems ", " >> " + "sales_agent_no" + "1");
                Log.e("SIGNUParems ", " >> " + "sales_agent_name" + "1");
                Log.e("SIGNUParems ", " >> " + "website_url" + "");
                Log.e("SIGNUParems ", " >> " + "facebook_url" + "");
                Log.e("SIGNUParems ", " >> " + "twitter_url" + "");
                Log.e("SIGNUParems ", " >> " + "linkdin_url" + "");
                Log.e("SIGNUPRESPONE ", " >> " + response);
                JSONObject object = new JSONObject(Jsondata);
                Log.e("SIGNUPRESPONEJSON ", " >> " + Jsondata);
                return Jsondata;


            } catch (Exception e) {
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

                        Intent i = new Intent(MerchantSignupSlider.this, MerchantBottumAct.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);

                    } else {
                        Toast.makeText(MerchantSignupSlider.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                String postReceiverUrl = BaseUrl.baseurl + "check_merchant.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("email", mer_email);
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
                        if (viewPager.getCurrentItem() == 4) {
                            Toast.makeText(MerchantSignupSlider.this, "This Phone number is not available", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(MerchantSignupSlider.this, "This Email is not available", Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }
}