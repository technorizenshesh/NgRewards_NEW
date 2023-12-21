package main.com.ngrewards.androidmigx;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.BuildConfig;
import main.com.ngrewards.QrCodeActivity;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.AccountTypeSelectionAct;
import main.com.ngrewards.activity.CommisionActivity;
import main.com.ngrewards.activity.EMIManualActivity;
import main.com.ngrewards.activity.EmployeesalesActivity;
import main.com.ngrewards.activity.MyCartDetail;
import main.com.ngrewards.activity.NetworkAct;
import main.com.ngrewards.activity.PreferenceConnector;
import main.com.ngrewards.activity.TransferToaFriend;
import main.com.ngrewards.activity.TutorialAct;
import main.com.ngrewards.activity.app.Config;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySavedCardInfo;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.draweractivity.ProfileActivity;
import main.com.ngrewards.draweractivity.SettingActivity;
import main.com.ngrewards.marchant.activity.MerchantNotificationActivity;

public class MainTabActivity extends AppCompatActivity {
    private static final float END_SCALE = 0.85f;
    public static  String DEEP_LINK_URL = "";
    public static String user_log_data = "", username_str, ngcash = "", user_id = "", currency_code = "",
            currency_sign = "", country_name = "", notification_data = "", notification_unseen_count, cart_unseen_count = "";
    public static String member_ngcash = "0";
    public final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("MEMBER_HOMEMEMBER_HOME", "----------------------------dfdd");
            if (intent.getStringExtra("object") != null) {
                try {
                    JSONObject data = new JSONObject(intent.getStringExtra("object"));
                    Log.e("MEMBER_HOMEMEMBER_HOME", "----------------------------dfdd" + data);
                    String member_id = data.getString("member_id");
                    String cart_id = data.getString("cart_id");
                    String split_amount_x = data.getString("split_amount_x");
                    //  String  merchant_business_name=data.getString("merchant_business_name");
                    String merchant_business_no = data.getString("merchant_business_no");
                    String merchant_id = data.getString("merchant_id");
                    String message = data.getString("message");
                    String type = data.getString("type");
                    String due_date = data.getString("due_date");
                    String order_id = data.getString("order_id");
                    int number_of_emi = Integer.parseInt(data.getString("number_of_emi"));
                    String str = "th";
                    if (number_of_emi == 0) str = "st";
                    if (number_of_emi == 1) str = "nd";
                    if (number_of_emi == 2) str = "rd";
                    Log.e("MEMBER_HOMEMEMBER_HOME", "-----------------------------messagemessage----" + message);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle(context.getString(R.string.payment));
                    alertDialog.setMessage(getString(R.string.reminder_for) + number_of_emi + str + context.getString(R.string.payment) + split_amount_x + context.getString(R.string.due_on) + due_date);
                    alertDialog.setPositiveButton(context.getString(R.string.pay_now), (dialog, which) -> {
                        dialog.dismiss();
                        Intent intentw = new Intent(getApplicationContext(), EMIManualActivity.class);
                        intentw.putExtra("object", data.toString());
                        context.startActivity(intentw);
                    });
                    alertDialog.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.cancel());
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                } catch (Exception e) {
                    Log.e(TAG, "onReceive: -----------------------------ddd" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    };
    private final boolean isVisible = false;
    public Dialog dialogSts;
    TextView ngcash_txt;
    int WhichIndex = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String scrsts = "";
    MySession mySession;
    public final BroadcastReceiver broadcastReceiver2 =
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
    TextView counter_wallet, counter_shedule, counter_order, counter_message;
    ScheduledExecutorService scheduleTaskExecutor;
    String currentVersion = "";
    CircleImageView user_img;
    boolean mSlideState = false;
    CircleImageView drwr_user_img;
    MySavedCardInfo mySavedCardInfo;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavView;
///////////234567890-
    private CoordinatorLayout contentView;
    private Boolean exit;
    private Dialog canceldialog;
    private String facebook_name;
    private String facebook_image;
    private String result = "";
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationview;
    private LinearLayout messagelay, rate_lay, profile_lay, employeesaleslay, setting_lay, logout, networklay, tutoriallay, transferlay;
    private TextView user_name, name, reqcount, cartcount;
    private ImageView notification, cartimg, qrcode;
    private LinearLayout commissionlay;
    private Myapisession myapisession;
    private String newcreate_user_name;
    private String craete_profile;
    private String createUserName;
    private String user_name1;
    private String dfgfd;
    private String dfgfd1;
    private ProgressBar progresbar;
    private String gender_str;
    private String age_str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab_new);
        Tools.reupdateResources(this);
        initToolbar();
        initNavigation();
        if (getIntent().getExtras() != null) {
            result = getIntent().getExtras().getString("result");
            if (result == null) {
                result = "";
            }
        }
        notification_unseen_count = "";
        cart_unseen_count = "";
        mySession = new MySession(this);
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
        if (mySession.getAppUpdate().equalsIgnoreCase("cancel")) {
            try {
                currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                Log.e("OnCreate", "Current version " + currentVersion);
                new GetVersionCode().execute();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("OnCreate EXC", "Current version " + currentVersion);
            }
            Log.e("OnCreate OUT", "Current version " + currentVersion);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Log.e("Get Notification >>", "NULL");
        } else {

            String message = bundle.getString("message");
            facebook_name = bundle.getString("facebook_name");
            facebook_image = bundle.getString("facebook_image");

            Log.e("Get Notification >>", "" + message);
            if (message == null || message.equalsIgnoreCase("") || message.equalsIgnoreCase("null")) {
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    String keys = jsonObject.getString("key").trim();
                    notification_data = message;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Bundle extra = getIntent().getExtras();
        exit = false;
        if (extra == null) {

        } else {
            scrsts = extra.getString("scrsts");
            if (scrsts == null || scrsts.equalsIgnoreCase("")) {
                if (extra.containsKey("WhichIndex")) {
                    WhichIndex = extra.getInt("WhichIndex", 0);
                }

            } else {
                if (scrsts.equalsIgnoreCase("activity")) {
                    WhichIndex = extra.getInt("WhichIndex", 2);
                } else {
                    WhichIndex = extra.getInt("WhichIndex", 0);
                }

            }
        }

        onCreate2();

    }

    private void onCreate2() {

        mySession = new MySession(this);
        Tools.reupdateResources(this);

        idinit();
        user_log_data = mySession.getKeyAlldata();
        dialogSts = new Dialog(MainTabActivity.this, R.style.DialogSlideAnim);

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

        craete_profile = PreferenceConnector.readString(MainTabActivity.this, PreferenceConnector.Create_Profile, "");
        // logout_status = PreferenceConnector.readString(MainTabActivity.this, PreferenceConnector.Logout_Status, "");
        //  fb_status = PreferenceConnector.readString(MainTabActivity.this, PreferenceConnector.Status_Facebook, "");
        //  fb_status111 = PreferenceConnector.readString(MainTabActivity.this, PreferenceConnector.Profile_com, "");
        //  Toast.makeText(this, "status : " + logout_status, Toast.LENGTH_SHORT).show();

        new MainTabActivity.GetProfile().execute();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        adddrawer();
        clcickev();
    }

    private void initToolbar() {
        //Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);


    }

    private void initNavigation() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationview);
        bottomNavView = findViewById(R.id.bottom_nav_view);
        bottomNavView.clearAnimation();
        BottomNavigationViewHelper.disableShiftMode(bottomNavView);
        contentView = findViewById(R.id.content_view);
        counter_message = findViewById(R.id.reqcount);
        user_img = findViewById(R.id.user_img);
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START);
                //  }
            }
        });
        /*R.id.nav_profile, R.id.nav_transfer_img, R.id.nav_network, R.id.nav_commission_ic, R.id.nav_employee_sales, R.id.nav_tutorilas, R.id.nav_messages, R.id.nav_ic_settings, R.id.nav_rate_us, R.id.nav_logout,*/
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.bottom_home, R.id.bottom_paybill, R.id.bottom_activity, R.id.bottom_message, R.id.bottom_invite).setDrawerLayout(drawer).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //  NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                Log.e(TAG, "onDestinationChanged: " + controller);
                Log.e(TAG, "onDestinationChanged: " + destination);
                Log.e(TAG, "onDestinationChanged: " + arguments);

            }
        });

        //  animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {
//        drawerLayout.setScrimColor(getResources().getColor(R.color.text_brown));
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (scheduleTaskExecutor == null) {

            } else {
                scheduleTaskExecutor.shutdown();
            }
            super.onBackPressed();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(broadcastReceiver2);

        if (scheduleTaskExecutor == null) {

        } else {
            scheduleTaskExecutor.shutdown();
        }

    }

/*    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (scheduleTaskExecutor == null) {

        } else {
            scheduleTaskExecutor.shutdown();
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("MEMBER_HOME"));
        Tools.reupdateResources(this);
        new MainTabActivity.GetProfile().execute();
        Tools.reupdateResources(this);
        registerReceiver(broadcastReceiver2, new IntentFilter(Config.PUSH_NOTIFICATION));
        try {
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
                            Glide.with(MainTabActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(drwr_user_img);
                            Glide.with(MainTabActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(user_img);
                        }

                        if (username == null || username.equalsIgnoreCase("") || username.equalsIgnoreCase("null")) {

                        }

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    new MyCounterVal().execute();
                }
            }, 0, 8, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "onResume: " + e.getCause());
        }
    }

    private void appUpdate() {
        canceldialog = new Dialog(MainTabActivity.this);
        canceldialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        canceldialog.setCancelable(false);
        canceldialog.setContentView(R.layout.appupdatelayout);
        canceldialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView yes_tv = (TextView) canceldialog.findViewById(R.id.yes_tv);
        final TextView no_tv = (TextView) canceldialog.findViewById(R.id.no_tv);
        final TextView body_tv = (TextView) canceldialog.findViewById(R.id.body_tv);

        body_tv.setText("" + getResources().getString(R.string.appupdateneed));
        no_tv.setText("" + getResources().getString(R.string.remindlater));
        yes_tv.setText("" + getResources().getString(R.string.ok));

        yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canceldialog.dismiss();
                final String appPackageName = BuildConfig.APPLICATION_ID; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySession.setAppUpdate("later");
                canceldialog.dismiss();
            }
        });

        if (canceldialog == null) {
            canceldialog.show();
        } else if (canceldialog.isShowing()) {

        } else {
            canceldialog.show();

        }
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


    /////234567890-

    private void clcickev() {
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Toast.makeText(MainTabActivity.this, "Successs!!!!!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainTabActivity.this,
                        MerchantNotificationActivity.class)
                        .putExtra("type", "member");
                startActivity(i);
            }
        });

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainTabActivity.this, QrCodeActivity.class);
                startActivity(i);
            }
        });

        profile_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainTabActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        employeesaleslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainTabActivity.this, EmployeesalesActivity.class);
                startActivity(i);
            }
        });

        setting_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainTabActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });

        networklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainTabActivity.this, NetworkAct.class);
                startActivity(i);
            }
        });


        tutoriallay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainTabActivity.this, TutorialAct.class);
                i.putExtra("type", "member");
                startActivity(i);
            }
        });


        transferlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainTabActivity.this, TransferToaFriend.class);
                startActivity(i);
            }
        });


        messagelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        Intent i = new Intent(MainTabActivity.this, MemberMessageAct.class);
                //        startActivity(i);
            }
        });


        commissionlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainTabActivity.this, CommisionActivity.class);
                startActivity(i);
            }
        });


        cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainTabActivity.this, MyCartDetail.class);
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

                Intent i = new Intent(MainTabActivity.this, AccountTypeSelectionAct.class);
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

        ngcash_txt = findViewById(R.id.ngcash_txt);
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

        //  setSupportActionBar(toolbar);
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

    @SuppressLint("StaticFieldLeak")
    private class MyCounterVal extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "msg_unseen_count.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", user_id);
                params.put("type", "Member");
                Log.e("Member Tab user_id >. ", " >>" + user_id);
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
                Log.e("MainTabCounter Hire", ">>>>>>>>>>>>" + response);
                return response;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
            } else if (result.isEmpty()) {
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        int message_unseen_count = 0;
                        String counter_message_int = jsonObject.getString("unseen_count");


                        //impliment by sagar panse //

                      /*  String delete_status = jsonObject.getString("delete_status");

                        if (delete_status.equalsIgnoreCase("Deactive")) {
                            mySession.signinusers(false);
                            mySession.logoutUser();
                            Intent i = new Intent(MainTabActivity.this, AccountTypeSelectionAct.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                        }*/

                        //impliment by sagar panse //

                        notification_unseen_count = jsonObject.getString("notification_unseen_count");
                        cart_unseen_count = jsonObject.getString("cart_count");
                        ngcash = jsonObject.getString("ngcash");

                        Log.e("notification >> ", " >> " + notification_unseen_count);
                        Intent j = new Intent("Unseen Count");
                        j.putExtra("noticount", notification_unseen_count);
                        j.putExtra("cartcount", cart_unseen_count);
                        j.putExtra("ngcash", ngcash);

                        sendBroadcast(j);

                        if (counter_message_int == null || counter_message_int.equalsIgnoreCase("")) {

                        } else {
                            message_unseen_count = Integer.parseInt(counter_message_int);
                        }

                        if (message_unseen_count != 0) {
                            counter_message.setText("" + counter_message_int);
                            counter_message.setVisibility(View.VISIBLE);
                        } else {
                            counter_message.setVisibility(View.GONE);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class GetVersionCode extends AsyncTask<Void, String, String> {
        //implimemnmt by sagar panse //
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id="
                        + MainTabActivity.this.getPackageName() + "&hl=en").timeout(30000).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").referrer("http://www.google.com").get();
                try {
                    if (document != null) {
                        Elements element = document.getElementsContainingOwnText("Current Version");
                        for (Element ele : element) {
                            if (ele.siblingElements() != null) {
                                Elements sibElemets = ele.siblingElements();
                                for (Element sibElemet : sibElemets) {
                                    newVersion = sibElemet.text();
                                }
                            }
                        }
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

            } catch (Throwable t) {
                Log.e("OSFP.News", t.getMessage(), t);
                // finish();
            }
            return newVersion;
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (currentVersion != null) {

                    try {
                        if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                            try {
                                appUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
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

            } catch (Exception e1) {

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
                        mySession = new MySession(MainTabActivity.this);
                        mySession.setValueOf(MySession.CountryId, country_id);
                        mySession.setValueOf(MySession.CurrencyCode, currency_code);
                        mySession.setValueOf(MySession.CurrencySign, currency_sign);
                        mySession.setValueOf(MySession.CountryName, country_name);
                     String   username = jsonObject1.getString("username");
                        String  id = jsonObject1.getString("id");
                        String   affiliate_number = jsonObject1.getString("affiliate_number");

                        Log.e(TAG, "onCreate:  country_id   ----  " + mySession.getValueOf(MySession.CountryId));
                        Log.e(TAG, "onCreate:  currency_code   ----  " + mySession.getValueOf(MySession.CurrencyCode));
                        Log.e(TAG, "onCreate:  currency_sign   ----  " + mySession.getValueOf(MySession.CurrencySign));
                        Log.e(TAG, "onCreate:  country_name    ----  " + mySession.getValueOf(MySession.CountryName));
                        DEEP_LINK_URL = "https://myngrewards.com/deep-link?affiliate_name=" + username + "&affiliate_no=" + id + "&how_invited_you=" + affiliate_number + "&country=" + mySession.getValueOf(MySession.CountryId) + "&source=app";
                        Log.e(TAG, "DEEP_LINK_URLDEEP_LINK_URLDEEP_LINK_URLDEEP_LINK_URL: "+DEEP_LINK_URL );
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
                        ngcash_txt.setText(mySession.getValueOf(MySession.CurrencySign) + jsonObject1.getString("member_ngcash"));

                        if (username_str == null || username_str.equalsIgnoreCase("")) {
                            user_name.setEnabled(true);
                            user_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        }

                        String image_url = jsonObject1.getString("member_image");

                        if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                            Glide.with(MainTabActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(user_img);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

}