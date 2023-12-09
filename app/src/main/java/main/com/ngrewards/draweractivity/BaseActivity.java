package main.com.ngrewards.draweractivity;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

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

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.QrCodeActivity;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.AccountTypeSelectionAct;
import main.com.ngrewards.activity.CommisionActivity;
import main.com.ngrewards.activity.EmployeesalesActivity;
import main.com.ngrewards.activity.MyCartDetail;
import main.com.ngrewards.activity.NetworkAct;
import main.com.ngrewards.activity.PreferenceConnector;
import main.com.ngrewards.activity.TransferToaFriend;
import main.com.ngrewards.activity.TutorialAct;
import main.com.ngrewards.activity.app.Config;
import main.com.ngrewards.androidmigx.MainTabActivity;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySavedCardInfo;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.marchant.activity.MerchantNotificationActivity;


public class BaseActivity extends AppCompatActivity {

    public static String member_ngcash = "0", currency_code = "", currency_sign = "",
            country_name = "";
    private final boolean isVisible = false;
    public Dialog dialogSts;
    boolean exit = false;
    MySession mySession;
    boolean mSlideState = false;
    CircleImageView user_img, drwr_user_img;
    MySavedCardInfo mySavedCardInfo;
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationview;
    private LinearLayout messagelay, rate_lay, profile_lay, employeesaleslay, setting_lay, logout, networklay, tutoriallay, transferlay;
    private TextView user_name, name, reqcount, cartcount, ngcash;
    private ImageView notification, cartimg, qrcode;
    private LinearLayout commissionlay;
    private Myapisession myapisession;
    private String newcreate_user_name;
    private String craete_profile;
    private String createUserName;
    private String user_name1;
    private String dfgfd;
    private String dfgfd1;
    private String user_id;
    private ProgressBar progresbar;
    private String gender_str;
    private String age_str;
    private String user_log_data;
    public final BroadcastReceiver broadcastReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    if (intent != null) {
                        String str = intent.getStringExtra("key");
                        Log.e("message>>>>>>>", str);
                        mySession = new MySession(getApplicationContext());
                        user_log_data = mySession.getKeyAlldata();
                        if (user_log_data == null) {

                        } else {

                            try {
                                JSONObject jsonObject = new JSONObject(user_log_data);
                                String message = jsonObject.getString("status");
                                if (message.equalsIgnoreCase("1")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                    user_id = jsonObject1.getString("id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        reqcount.setVisibility(View.GONE);
                        new GetProfile().execute();
                    }
                }
            };
    private String username_str;
    private String name123;
    private String name1234;
    private String logout_status;
    private String fb_status;
    private String fb_status111;
    private View no_tv;
    private View yes_tv;

  /*  private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                Bundle bundle = intent.getExtras();
                Log.e("bundle>>>", "" + bundle);

                if (bundle != null) {
                    String result = intent.getExtras().getString("noticount");
                    String cartcounts = intent.getExtras().getString("cartcount");
                    member_ngcash = intent.getExtras().getString("ngcash");

                    Log.e("BroadCast", ">>" + result);

                    if (result == null || result.equalsIgnoreCase("") || result.equalsIgnoreCase("0")) {
                        reqcount.setVisibility(View.GONE);
                        MainTabActivity.notification_unseen_count = "";
                    } else {      //implemenbt by sagar panse //
                        reqcount.setVisibility(View.VISIBLE);
                        reqcount.setText("" + MainTabActivity.notification_unseen_count);
                    }

                    if (cartcounts == null || cartcounts.equalsIgnoreCase("") || cartcounts.equalsIgnoreCase("0")) {
                        cartcount.setVisibility(View.GONE);
                        MainTabActivity.cart_unseen_count = "";
                    } else {
                        cartcount.setVisibility(View.VISIBLE);
                        cartcount.setText("" + MainTabActivity.cart_unseen_count);
                    }

                    if (member_ngcash == null || member_ngcash.equalsIgnoreCase("") || member_ngcash.equalsIgnoreCase("null") || member_ngcash.equalsIgnoreCase("0")) {
                    } else {

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mySession = new MySession(this);
        Tools.reupdateResources(this);

        idinit();
        user_log_data = mySession.getKeyAlldata();
        dialogSts = new Dialog(BaseActivity.this, R.style.DialogSlideAnim);

        if (user_log_data == null) {

        } else {

            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        myapisession = new Myapisession(this);
        mySavedCardInfo = new MySavedCardInfo(this);
        drawer_layout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        mySession = new MySession(this);

        craete_profile = PreferenceConnector.readString(BaseActivity.this, PreferenceConnector.Create_Profile, "");
        logout_status = PreferenceConnector.readString(BaseActivity.this, PreferenceConnector.Logout_Status, "");
        fb_status = PreferenceConnector.readString(BaseActivity.this, PreferenceConnector.Status_Facebook, "");
        fb_status111 = PreferenceConnector.readString(BaseActivity.this, PreferenceConnector.Profile_com, "");
        Toast.makeText(this, "status : " + logout_status, Toast.LENGTH_SHORT).show();

        new GetProfile().execute();
        idinitui();

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if (extras == null) {

                newcreate_user_name = null;

            } else {
                newcreate_user_name = extras.getString("create_user_name");
            }

        } else {
            newcreate_user_name = (String) savedInstanceState.getSerializable("create_user_name");
        }

        user_log_data = mySession.getKeyAlldata();

        if (user_log_data == null) {

        } else {

            try {

                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {

                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    if (jsonObject1.has("member_ngcash")) {
                        member_ngcash = jsonObject1.getString("member_ngcash");

                        if (member_ngcash == null || member_ngcash.equalsIgnoreCase("0") || member_ngcash.equalsIgnoreCase("") || member_ngcash.equalsIgnoreCase("0.0") || member_ngcash.equalsIgnoreCase("null")) {
                            //ngcash.setText("$0.00");
                        } else {
                            // ngcash.setText("$" + member_ngcash);
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adddrawer();
        clcickev();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new GetProfile().execute();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void idinitui() {

        Toast.makeText(this, "status : " + logout_status, Toast.LENGTH_SHORT).show();

        if (!craete_profile.equals("craete_profile")) {

            dfgfd = PreferenceConnector.readString(BaseActivity.this, PreferenceConnector.UserNAme, "");
            dfgfd1 = PreferenceConnector.readString(BaseActivity.this, PreferenceConnector.UserNAme1, "");

            user_name.setText(dfgfd);
            name.setText("@" + dfgfd1);

            if (logout_status.equals("true")) {

                createUserName();

            } else {
                dialogSts.dismiss();
            }

            if (myapisession.equals("")) {

            }

        } else {

            dialogSts = new Dialog(BaseActivity.this, R.style.DialogSlideAnim);
        }


    }

    @Override
    protected void onResumeFragments() {
        mySession = new MySession(this);

        String user_log_data = mySession.getKeyAlldata();

        if (user_log_data == null) {

        } else {

            try {

                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");

                if (message.equalsIgnoreCase("1")) {

                    Log.e("jsonObject12345", "" + jsonObject);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    String business_name = jsonObject1.getString("fullname");
                    String username = jsonObject1.getString("affiliate_name");

                    String image_url = jsonObject1.getString("member_image");
                    if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                        Glide.with(BaseActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(drwr_user_img);
                        Glide.with(BaseActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(user_img);
                    }

                    if (username == null || username.equalsIgnoreCase("") || username.equalsIgnoreCase("null")) {

                    }

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }
        }
        super.onResumeFragments();
    }

    private void clcickev() {
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Toast.makeText(BaseActivity.this, "Successs!!!!!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(BaseActivity.this,
                        MerchantNotificationActivity.class)
                        .putExtra("type", "member");
                startActivity(i);
            }
        });

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivity.this, QrCodeActivity.class);
                startActivity(i);
            }
        });

        profile_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        employeesaleslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivity.this, EmployeesalesActivity.class);
                startActivity(i);
            }
        });

        setting_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });

        networklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivity.this, NetworkAct.class);
                startActivity(i);
            }
        });


        tutoriallay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivity.this, TutorialAct.class);
                i.putExtra("type", "member");
                startActivity(i);
            }
        });


        transferlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivity.this, TransferToaFriend.class);
                startActivity(i);
            }
        });


        messagelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        Intent i = new Intent(BaseActivity.this, MemberMessageAct.class);
                //        startActivity(i);
            }
        });


        commissionlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivity.this, CommisionActivity.class);
                startActivity(i);
            }
        });


        cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivity.this, MyCartDetail.class);
                startActivity(i);
            }
        });


        rate_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=main.com.ngrewards"));
                    startActivity(intent);

                } catch (Exception e) {

                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.employee_id, "");
                PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.employee_name, "");
/*
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }*/

                mySession.signinusers(false);
                mySavedCardInfo.clearCardData();
                myapisession.setKeyAddressdata("");
                myapisession.setKeyCartitem("");
                myapisession.setProductdata("");
                myapisession.setKeyOffercate("");

                Intent i = new Intent(BaseActivity.this, AccountTypeSelectionAct.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtra("logout_status", "false");
                startActivity(i);
                finish();

            }
        });

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSlideState) {
                    drawer_layout.openDrawer(GravityCompat.END);
                } else {
                    drawer_layout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void idinit() {

        ngcash = findViewById(R.id.ngcash);
        progresbar = findViewById(R.id.progresbar);
        cartcount = findViewById(R.id.cartcount);
        reqcount = findViewById(R.id.reqcount);

        if (MainTabActivity.notification_unseen_count == null || MainTabActivity.notification_unseen_count.equalsIgnoreCase("") || MainTabActivity.notification_unseen_count.equalsIgnoreCase("0")) {
            reqcount.setVisibility(View.GONE);
        } else {
            reqcount.setVisibility(View.VISIBLE);
            reqcount.setText("" + MainTabActivity.notification_unseen_count);
        }

        if (MainTabActivity.cart_unseen_count == null || MainTabActivity.cart_unseen_count.equalsIgnoreCase("") || MainTabActivity.cart_unseen_count.equalsIgnoreCase("0")) {
            cartcount.setVisibility(View.GONE);
        } else {
            cartcount.setVisibility(View.VISIBLE);
            cartcount.setText("" + MainTabActivity.cart_unseen_count);
        }

        cartimg = findViewById(R.id.cartimg);
        commissionlay = findViewById(R.id.commissionlay);
        messagelay = findViewById(R.id.messagelay);
        notification = findViewById(R.id.notification);
        qrcode = findViewById(R.id.qrcode);
        name = findViewById(R.id.name);
        // reqcount =  findViewById(R.id.reqcount);
        rate_lay = findViewById(R.id.rate_lay);
        transferlay = findViewById(R.id.transferlay);
        tutoriallay = findViewById(R.id.tutoriallay);
        networklay = findViewById(R.id.networklay);
        drwr_user_img = findViewById(R.id.drwr_user_img);
        user_name = findViewById(R.id.user_name);
        logout = findViewById(R.id.logout);
        profile_lay = findViewById(R.id.profile_lay);
        employeesaleslay = findViewById(R.id.employeesaleslay);
        user_img = findViewById(R.id.user_img);
        setting_lay = findViewById(R.id.setting_lay);

    }

    private void adddrawer() {

        setSupportActionBar(toolbar);
        navigationview = findViewById(R.id.navigationview);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        drawer_layout.setDrawerListener(new ActionBarDrawerToggle(this,
                drawer_layout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideState = false;

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSlideState = true;

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetProfile().execute();
        Tools.reupdateResources(this);
        registerReceiver(broadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));

    }

    public void createUserName() {

        dialogSts = new Dialog(BaseActivity.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setContentView(R.layout.custom_popup_create_memberuser);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        no_tv = dialogSts.findViewById(R.id.no_tv);
        yes_tv = dialogSts.findViewById(R.id.yes_tv);

        yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogSts.dismiss();
                Intent i = new Intent(BaseActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
            }
        });

        dialogSts.show();
    }

    private class GetProfile extends AsyncTask<String, String, String> {
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

                String postReceiverUrl = BaseUrl.baseurl + "member_profile.php?";

                URL url = new URL(postReceiverUrl);

                Map<String, Object> params = new LinkedHashMap<>();

                Log.e("member_id", user_id);
                params.put("member_id", user_id);

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

                Log.e("BASE ACT VGetProfile Response", ">>>>>>>>>>>>" + response);

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
                    String message = jsonObject.getString("status");

                    if (message.equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                        String unseen_count = jsonObject1.getString("unseen_count");
                        String country_id = jsonObject1.getString("country_id");
                        currency_code = jsonObject1.getString("currency_code");
                        currency_sign = jsonObject1.getString("currency_sign");
                        country_name = jsonObject1.getString("country_name");
                        mySession = new MySession(BaseActivity.this);
                        mySession.setValueOf(MySession.CountryId, country_id);
                        mySession.setValueOf(MySession.CurrencyCode, currency_code);
                        mySession.setValueOf(MySession.CurrencySign, currency_sign);
                        mySession.setValueOf(MySession.CountryName, country_name);
                        Log.e(TAG, "onCreate:  country_id   ----  " + mySession.getValueOf(MySession.CountryId));
                        Log.e(TAG, "onCreate:  currency_code   ----  " + mySession.getValueOf(MySession.CurrencyCode));
                        Log.e(TAG, "onCreate:  currency_sign   ----  " + mySession.getValueOf(MySession.CurrencySign));
                        Log.e(TAG, "onCreate:  country_name    ----  " + mySession.getValueOf(MySession.CountryName));

                        if (unseen_count.equals("0")) {
                            reqcount.setVisibility(View.GONE);

                        } else {
                            reqcount.setVisibility(View.VISIBLE);
                            reqcount.setText("" + unseen_count);
                        }


                        if (jsonObject1.getString("gender") != null) {
                            gender_str = jsonObject1.getString("gender").trim();
                        }

                        if (jsonObject1.getString("age") != null) {
                            age_str = jsonObject1.getString("age").trim();
                        }

                        name.setText("" + jsonObject1.getString("fullname"));
                        username_str = jsonObject1.getString("affiliate_name");

                        user_name.setText("" + jsonObject1.getString("username"));
                        ngcash.setText(mySession.getValueOf(MySession.CurrencySign) + jsonObject1.getString("member_ngcash"));

                        if (username_str == null || username_str.equalsIgnoreCase("")) {
                            user_name.setEnabled(true);
                            user_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        }

                        String image_url = jsonObject1.getString("member_image");

                        if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                            Glide.with(BaseActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(user_img);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
