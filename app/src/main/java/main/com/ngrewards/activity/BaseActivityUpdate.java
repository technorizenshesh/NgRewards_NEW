/*
package main.com.ngrewards.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.QrCodeActivity;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.androidmigx.MainTabActivity;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySavedCardInfo;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.draweractivity.ProfileActivity;
import main.com.ngrewards.draweractivity.SettingActivity;


public class BaseActivityUpdate extends AppCompatActivity {

    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationview;
    boolean exit = false;
    MySession mySession;
    boolean mSlideState = false;
    private LinearLayout messagelay, rate_lay, profile_lay, employeesaleslay, setting_lay, logout, networklay, tutoriallay, transferlay;
    CircleImageView user_img, drwr_user_img;
    private TextView user_name, name, reqcount, cartcount, ngcash;
    private ImageView notification, cartimg, qrcode;
    private LinearLayout commissionlay;
    MySavedCardInfo mySavedCardInfo;
    private Myapisession myapisession;
    public static String member_ngcash = "0";
    private boolean isVisible = false;
    private String newcreate_user_name;
    private String craete_profile;
    private Dialog dialogSts;
    private String createUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        mySavedCardInfo = new MySavedCardInfo(this);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        idinit();
        craete_profile =  PreferenceConnector.readString(BaseActivityUpdate.this, PreferenceConnector.Create_Profile,"");


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {

                newcreate_user_name = null;
            } else {
                newcreate_user_name = extras.getString("create_user_name");
            }
        } else {
            newcreate_user_name     = (String) savedInstanceState.getSerializable("create_user_name");
        }

        String user_log_data = mySession.getKeyAlldata();

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
                            ngcash.setText(mySession.getValueOf(MySession.CurrencySign) +"0.00");
                        } else {
                            ngcash.setText(mySession.getValueOf(MySession.CurrencySign)  + member_ngcash);
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

    private void clcickev() {

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivityUpdate.this, NotificationActivity.class);
                startActivity(i);
            }
        });


        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivityUpdate.this, QrCodeActivity.class);
                startActivity(i);
            }
        });

        profile_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivityUpdate.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        employeesaleslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivityUpdate.this, EmployeesalesActivity.class);
                startActivity(i);
            }
        });

        setting_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivityUpdate.this, SettingActivity.class);
                startActivity(i);
            }
        });

        networklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivityUpdate.this, NetworkAct.class);
                startActivity(i);
            }
        });

        tutoriallay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivityUpdate.this, TutorialAct.class);
                i.putExtra("type", "member");
                startActivity(i);
            }
        });

        transferlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivityUpdate.this, TransferToaFriend.class);
                startActivity(i);
                */
/*Intent i = new Intent(BaseActivity.this,MemberTransfer.class);
                startActivity(i);*//*

            }
        });

        messagelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivityUpdate.this, MemberMessageAct.class);
                startActivity(i);
            }
        });

        commissionlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivityUpdate.this, CommisionActivity.class);
                startActivity(i);
            }
        });

        cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivityUpdate.this, MyCartDetail.class);
                startActivity(i);
            }
        });

        rate_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(BaseActivity.this, "This button is work when our application is on google play store..", Toast.LENGTH_LONG).show();
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
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }

                mySession.signinusers(false);
                mySavedCardInfo.clearCardData();
                myapisession.setKeyAddressdata("");
                myapisession.setKeyCartitem("");
                myapisession.setProductdata("");
                myapisession.setKeyOffercate("");
                Intent i = new Intent(BaseActivityUpdate.this, AccountTypeSelectionAct.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSlideState) {
                    drawer_layout.openDrawer(Gravity.END);

                } else {
                    drawer_layout.openDrawer(Gravity.START);
                }
            }
        });
    }
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    private void idinit() {

        ngcash = findViewById(R.id.ngcash);
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
        navigationview = (NavigationView) findViewById(R.id.navigationview);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        // drawer_layout.setDrawerListener(actionBarDrawerToggle);
        drawer_layout.setDrawerListener(new ActionBarDrawerToggle(this,
                drawer_layout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideState = false;//is Closed
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSlideState = true;//is Opened
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

        finish();

        if (exit) {

            finish(); // finish activity

        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

      private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {

              try {

                  Bundle bundle = intent.getExtras();

                  if (bundle != null) {

                      String result = intent.getExtras().getString("noticount");
                    String cartcounts = intent.getExtras().getString("cartcount");
                    member_ngcash = intent.getExtras().getString("ngcash");

                    Log.e("BroadCast", " >>" + result);

                    if (result == null || result.equalsIgnoreCase("") || result.equalsIgnoreCase("0")) {
                        reqcount.setVisibility(View.GONE);
                        MainTabActivity.notification_unseen_count = "";

                    } else {
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
                        ngcash.setText(mySession.getValueOf(MySession.CurrencySign) +"0.00");
                    } else {
                        ngcash.setText(mySession.getValueOf(MySession.CurrencySign)  + member_ngcash);
                    }
                }

            } catch (Exception e) {

            }

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        isVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.reupdateResources(this);

        isVisible = true;
        registerReceiver(broadcastReceiver, new IntentFilter("Unseen Count"));

        mySession = new MySession(this);

        String user_log_data = mySession.getKeyAlldata();

        if (user_log_data == null) {

        } else {

            try {

                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");


                if (message.equalsIgnoreCase("1")) {


                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    Log.e("jsonObject12",""+jsonObject1);

                    String business_name = jsonObject1.getString("fullname");
                    String username = jsonObject1.getString("username");

                    name.setText("" + business_name);
                    user_name.setText("@" + username);

                    String image_url = jsonObject1.getString("member_image");

                    if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {

                    }

                    if(username == null || username.equalsIgnoreCase("") || username.equalsIgnoreCase("null")) {

                        if(!craete_profile.equals("craete_profile")) {

                            createUserName();

                        }

                        else {

                        }

                    }

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }
        }
    }


    private void createUserName1() {

        dialogSts = new Dialog(BaseActivityUpdate.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.custom_popup_create_memberuser);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView no_tv = dialogSts.findViewById(R.id.no_tv);
        TextView yes_tv = dialogSts.findViewById(R.id.yes_tv);

        LinearLayout linear_tax = dialogSts.findViewById(R.id.linear_tax);
        linear_tax.setVisibility(View.GONE);
        yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent i = new Intent(BaseActivityUpdate.this, ProfileActivity.class);
                startActivity(i);

            }
        });

        no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();


            }
        });

        try {

            dialogSts.dismiss();

        } catch (Exception e) {


        }


    }

    private void createUserName() {

        dialogSts = new Dialog(BaseActivityUpdate.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.custom_popup_create_memberuser);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView no_tv = dialogSts.findViewById(R.id.no_tv);
        TextView yes_tv = dialogSts.findViewById(R.id.yes_tv);

        yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent i = new Intent(BaseActivityUpdate.this, ProfileActivity.class);
                startActivity(i);

            }
        });

        no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();


            }
        });

        try {

            dialogSts.show();
        } catch (Exception e) {

        }

    }

}
*/
