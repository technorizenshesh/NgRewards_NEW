package main.com.ngrewards.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.beanclasses.MarchantBean;
import main.com.ngrewards.beanclasses.MerchantListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.merchant_fragment.MerchantAboutFrag;
import main.com.ngrewards.merchant_fragment.MerchantItemsFrag;
import main.com.ngrewards.merchant_fragment.MerchantOffersFrag;
import main.com.ngrewards.merchant_fragment.MerchantReviewsFrag;
import main.com.ngrewards.merchant_fragment.MerchatPhotoFrag;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchantDetailAct extends AppCompatActivity {

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    public static String latitude_str = "0", longitude_str = "0", merchant_id = "", merchant_img_str = "", merchant_name_str = "";
    public static ArrayList<MerchantListBean> merchantListBeanArrayList;
    private final String phone_number_str = "";
    private final String business_category_name_str = "";
    private final String distance_str = "";
    private final String merchant_location_str = "";
    LocationManager locationManager;
    Location location;
    GPSTracker gpsTracker;
    KeyStore keyStore;
    String KEY_NAME = "NgRewards", status_touchid = "", merchant_contact_name = "";
    Cipher cipher;
    TextView textView, open_close_status;
    MySession mySession;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ScrollView scrollView;
    private RelativeLayout backlay;
    private String user_id = "";
    private String merchant_number_str = "";
    private TextView businesscategory_name, distance_tv, merchant_name_head, merchant_name, merchant_number, location_tv, paybill;
    private ProgressBar progresbar;
    private ImageView user_img;
    private double latitude = 0, longitude = 0;
    private TextView sss;
    private ImageView messagess;
    private String employee_sales_id;
    private String employee_slaes_name;
    private String opeaning_time;
    private String closing_time;

    private Boolean navigateTest = false;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_merchant_detail);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MerchantDetailAct.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MerchantDetailAct.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        checkGps();

        mySession = new MySession(MerchantDetailAct.this);
        String user_log_data = mySession.getKeyAlldata();
        Log.e("USER DATA", ">> " + user_log_data);
        if (user_log_data == null) {

        } else {

            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    status_touchid = jsonObject1.getString("touch_status");


                }
            } catch (JSONException ee) {
                ee.printStackTrace();

            }
        }
        idinita();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            navigateTest = false;

            user_id = bundle.getString("user_id");

            if (user_id.contains("demo")) {
                navigateTest = true;
            }

            merchant_id = bundle.getString("merchant_id");
            merchant_name_str = bundle.getString("merchant_name");
            merchant_number_str = bundle.getString("merchant_number");
            merchant_img_str = bundle.getString("merchant_img");

            employee_sales_id = bundle.getString("employee_sales_id");
            employee_slaes_name = bundle.getString("employee_slaes_name");

            opeaning_time = bundle.getString("opeaning_time");
            closing_time = bundle.getString("closing_time");

            merchant_name.setText("" + merchant_name_str);
            merchant_name_head.setText("" + merchant_name_str);
            merchant_number.setText("" + merchant_number_str);

            Log.e("merchant_image >> ", " >> " + merchant_img_str);
            if (merchant_img_str != null && !merchant_img_str.equalsIgnoreCase("") && !merchant_img_str.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Glide.with(MerchantDetailAct.this).load(merchant_img_str).placeholder(R.drawable.placeholder).into(user_img);

            }

            SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a");

            String timeav = formatDate.format(new Date());
            String formattedDate = timeav.replace("a.m.", "AM").replace("p.m.", "PM");

            Log.e("Current Time", " ." + formattedDate);

            getMerchantsDetail(merchant_id, formattedDate);
        }

        clickevent();
    }

    public String getOpeningTime() {
        return opeaning_time;
    }

    public String getClosing_time() {
        return closing_time;
    }

    private void getMerchantsDetail(String merchant_id, String time) {

        Log.e("USER ID==" + user_id, " > MERchant Id " + merchant_id);
        Log.e("latitude==" + latitude, " > longitude " + longitude);
        Log.e("time==", "===" + time);

        progresbar.setVisibility(View.VISIBLE);
        merchantListBeanArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMerchantDetail(user_id, merchant_id, "" + latitude, "" + longitude, time);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("Merchant Detail >", " >" + responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {

                            MarchantBean successData = new Gson().fromJson(responseData, MarchantBean.class);

                            merchantListBeanArrayList.addAll(successData.getResult());

                        }

                        if (merchantListBeanArrayList != null && !merchantListBeanArrayList.isEmpty()) {
                            if (merchantListBeanArrayList.get(0).getOpenCloseStatus() != null) {

                                if (merchantListBeanArrayList.get(0).getOpenCloseStatus().equalsIgnoreCase("OPEN")) {
                                    open_close_status.setText("" + getString(R.string.open));
                                    open_close_status.setTextColor(getResources().getColor(R.color.darkgreen));
                                } else {
                                    open_close_status.setTextColor(getResources().getColor(R.color.red));
                                    open_close_status.setText("" + merchantListBeanArrayList.get(0).getOpenCloseStatus());
                                }
                            } else {
                                open_close_status.setTextColor(getResources().getColor(R.color.red));
                                open_close_status.setText(R.string.close);
                            }

                        }
                        setupviewpager();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                progresbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void setupviewpager() {

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void clickevent() {

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        paybill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantDetailAct.this, ManualActivity.class);
                i.putExtra("user_id", user_id);
                i.putExtra("merchant_id", merchant_id);
                i.putExtra("merchant_name", merchant_name_str);
                i.putExtra("merchant_number", merchant_number_str);
                i.putExtra("employee_sales_id", employee_sales_id);
                i.putExtra("employee_slaes_name", employee_slaes_name);
                i.putExtra("type", "paybill");
                startActivity(i);
            }
        });
    }

    private void idinita() {
        open_close_status = findViewById(R.id.open_close_status);
        paybill = findViewById(R.id.paybill);
        businesscategory_name = findViewById(R.id.businesscategory_name);
        businesscategory_name = findViewById(R.id.businesscategory_name);
        distance_tv = findViewById(R.id.distance_tv);
        user_img = findViewById(R.id.user_img);
        progresbar = findViewById(R.id.progresbar);
        location_tv = findViewById(R.id.location_tv);
        merchant_name = findViewById(R.id.merchant_name);
        merchant_number = findViewById(R.id.merchant_number);
        merchant_name_head = findViewById(R.id.merchant_name_head);
        backlay = findViewById(R.id.backlay);
        scrollView = findViewById(R.id.scrollView);
        tabLayout = findViewById(R.id.tabs);
        messagess = findViewById(R.id.messages);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);

        messagess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantDetailAct.this, MemberChatAct.class);
                i.putExtra("receiver_id", merchant_id);
                i.putExtra("type", "Member");
                i.putExtra("receiver_type", "Merchant");
                i.putExtra("receiver_fullname", merchant_name_str);
                i.putExtra("receiver_img", merchant_img_str);
                i.putExtra("receiver_name", merchant_name_str);
                startActivity(i);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MerchantAboutFrag(), getResources().getString(R.string.about));
        adapter.addFragment(new MerchatPhotoFrag(), getResources().getString(R.string.photo));
        adapter.addFragment(new MerchantOffersFrag(), getResources().getString(R.string.offers));
        adapter.addFragment(new MerchantItemsFrag(), getResources().getString(R.string.items));
        adapter.addFragment(new MerchantReviewsFrag(), getResources().getString(R.string.reviews));

        viewPager.setAdapter(adapter);

        if (navigateTest) {
            navigateTest = false;
            viewPager.setCurrentItem(2);
        }

    }

    private void checkGps() {
        gpsTracker = new GPSTracker(MerchantDetailAct.this);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fingerPrintLay() {
        final Dialog dialogSts = new Dialog(MerchantDetailAct.this, R.style.DialogSlideAnim);
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
                            FingerprintHandler helper = new FingerprintHandler(MerchantDetailAct.this, textView, "Payment");
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