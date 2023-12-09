package main.com.ngrewards.marchant.draweractivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.AccountTypeSelectionAct;
import main.com.ngrewards.activity.MerchantMenuSetting;
import main.com.ngrewards.activity.TutorialAct;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.marchant.activity.MerchantNotificationActivity;
import main.com.ngrewards.marchant.activity.MyQrCodeActivity;

public class MerchantBaseActivity extends AppCompatActivity {
    public static TextView reqcounft;
    boolean exit = false;
    MySession mySession;
    boolean mSlideState = false;
    CircleImageView user_img, drwr_user_img;
    private DrawerLayout drawer_layout_mer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private androidx.appcompat.widget.Toolbar toolbar_mer;
    private NavigationView navigationview_mer;
    private LinearLayout rate_lay, reviewlay, offeres_lay, tutoriallay, profile_lay, setting_lay, menu_lay, logout;
    private TextView user_name, merchant_number;
    private ImageView myqr_but, notification;
    private Myapisession myapisession;
    private String user_id;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_base);
        mySession = new MySession(this);
        String user_log_data = mySession.getKeyAlldata();
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
        drawer_layout_mer = (DrawerLayout) findViewById(R.id.drawer_layout_mer);
        toolbar_mer = (Toolbar) findViewById(R.id.toolbar_mer);
        myapisession = new Myapisession(this);
        idinit();
        adddrawer();
        clcickev();
    }

    private void clcickev() {

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (AccessToken.getCurrentAccessToken() != null) {
//                    LoginManager.getInstance().logOut();
//                }
                myapisession.setProductdata("");
                myapisession.setKeyOffercate("");
                mySession.signinusers(false);

                Intent i = new Intent(MerchantBaseActivity.this, AccountTypeSelectionAct.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });

        tutoriallay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantBaseActivity.this, TutorialAct.class);
                i.putExtra("type", "merchant");
                startActivity(i);
            }
        });

        offeres_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantBaseActivity.this, OffersActivity.class);
                startActivity(i);
            }
        });

        reviewlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantBaseActivity.this, ReviewActivity.class);
                startActivity(i);
            }
        });

        profile_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantBaseActivity.this, MerProfileActivity.class);
                startActivity(i);
            }
        });
        rate_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MerchantBaseActivity.this,"This button is work when our application is on google play store..",Toast.LENGTH_LONG).show();
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=main.com.ngrewards"));
                    startActivity(intent);
                } catch (Exception e) {

                }
            }
        });

        setting_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantBaseActivity.this, MerSettingActivity.class);
                startActivity(i);
            }
        });

        //implimen here //
        menu_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantBaseActivity.this, MerchantMenuSetting.class);
                startActivity(i);
            }
        });

        myqr_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantBaseActivity.this, MyQrCodeActivity.class);
                startActivity(i);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  UpdateStatus();
                Intent i = new Intent(MerchantBaseActivity.this, MerchantNotificationActivity.class)
                        .putExtra("type", "merchant");
                startActivity(i);
            }

        });

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSlideState) {
                    drawer_layout_mer.openDrawer(GravityCompat.END);

                } else {
                    drawer_layout_mer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void UpdateStatus() {
        new UpdateStatus().execute();
        // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    private void idinit() {
        notification = findViewById(R.id.notification);
        reqcounft = findViewById(R.id.reqcounft);
        myqr_but = findViewById(R.id.myqr_but);
        rate_lay = findViewById(R.id.rate_lay);
        offeres_lay = findViewById(R.id.offeres_lay);
        reviewlay = findViewById(R.id.reviewlay);
        tutoriallay = findViewById(R.id.tutoriallay);
        user_name = findViewById(R.id.user_name);
        merchant_number = findViewById(R.id.merchant_number);
        logout = findViewById(R.id.logout);
        drwr_user_img = findViewById(R.id.drwr_user_img);
        profile_lay = findViewById(R.id.profile_lay);
        user_img = findViewById(R.id.user_img);
        menu_lay = findViewById(R.id.menu_lay);
        setting_lay = findViewById(R.id.setting_lay);

    }

    private void adddrawer() {
        setSupportActionBar(toolbar_mer);
        navigationview_mer = (NavigationView) findViewById(R.id.navigationview_mer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout_mer, toolbar_mer, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        drawer_layout_mer.setDrawerListener(new ActionBarDrawerToggle(this,
                drawer_layout_mer,
                toolbar_mer,
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
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, getString(R.string.press_back_again_to_exit),
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

    @Override
    protected void onResume() {
        super.onResume();
        Tools.reupdateResources(this);

        mySession = new MySession(this);

        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    String business_name = jsonObject1.getString("business_name");
                    String contact_name = jsonObject1.getString("contact_name");
                    String business_no = jsonObject1.getString("business_no");
                    merchant_number.setText("" + business_no);
                    if (business_name == null || business_name.equalsIgnoreCase("")) {
                        user_name.setText("" + contact_name);
                    } else {
                        user_name.setText("" + business_name);

                    }

                    String image_url = jsonObject1.getString("merchant_image");
                    if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                        Glide.with(MerchantBaseActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(drwr_user_img);
                        Glide.with(MerchantBaseActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(user_img);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdateStatus extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
//https://international.myngrewards.com/demo/wp-content/plugins/webservice/add_member_card_details.php?member_id=1&card_name=ks&card_number=122334455&expiry_date=12/08&expiry_year=2020
            try {
                String postReceiverUrl = BaseUrl.baseurl + "update_chat_status.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("reciever_id", user_id);
                params.put("type", "merchant");

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
                Log.e("Json Add Response", ">>>>>>>>>>>>" + response);
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
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(MerchantBaseActivity.this, getResources().getString(R.string.status), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MerchantBaseActivity.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {

                }
            }
        }
    }
}



/*
Complete the feedback of given

Change the status of screen a
*/