/*
package main.com.ngrewards.bottumtab;

import static android.content.ContentValues.TAG;
import static main.com.ngrewards.marchant.draweractivity.MerchantBaseActivity.reqcounft;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.BuildConfig;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.EMIManualActivity;
import main.com.ngrewards.activity.MemberMessageAct;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;


public class MainTabActivity extends TabActivity {
    int WhichIndex = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String WhichLanguage = "";
    private Boolean exit;
    String scrsts = "";
    MySession mySession;
    public static String user_log_data = "", ngcash = "", user_id = "", currency_code = "",currency_sign="",
    country_name="",  notification_data = "", notification_unseen_count, cart_unseen_count = "";
    TextView counter_wallet, counter_shedule, counter_order, counter_message;
    ScheduledExecutorService scheduleTaskExecutor;
    String currentVersion = "";
    private Dialog canceldialog;
    private String facebook_name;
    private String facebook_image;
    private String result = "";

 public final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("MEMBER_HOMEMEMBER_HOME",
                    "----------------------------dfdd");
            if(intent.getStringExtra("object") != null) {
                try {
                    JSONObject data = new JSONObject(intent.getStringExtra("object"));
                    Log.e("MEMBER_HOMEMEMBER_HOME",
                            "----------------------------dfdd" + data);
                String  member_id= data.getString("member_id");
                String  cart_id=data.getString("cart_id");
                String  split_amount_x=data.getString("split_amount_x");
              //  String  merchant_business_name=data.getString("merchant_business_name");
                String  merchant_business_no=data.getString("merchant_business_no");
                String  merchant_id= data.getString("merchant_id");
                String  message= data.getString("message");
                String  type= data.getString("type");
                String  due_date= data.getString("due_date");
                String  order_id= data.getString("order_id");
                int  number_of_emi= Integer.parseInt(data.getString("number_of_emi"));
                    String str = "th";
                    if (number_of_emi == 0) str = "st";
                    if (number_of_emi == 1) str = "nd";
                    if (number_of_emi == 2) str = "rd";
                    Log.e("MEMBER_HOMEMEMBER_HOME",
                            "-----------------------------messagemessage----"+message);
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
                    alertDialog.setTitle(context.getString(R.string.payment));
                    alertDialog.setMessage(
                            getString(R.string.reminder_for)+number_of_emi+str+context.getString(R.string.payment)+split_amount_x+context.getString(R.string.due_on)+due_date);
                    alertDialog.setPositiveButton(context.getString(R.string.pay_now), (dialog, which) -> {
                         dialog.dismiss();
                       Intent intentw=new Intent(getApplicationContext(), EMIManualActivity.class);
                       intentw.putExtra("object",data.toString());
                        context.startActivity(intentw);
                    });
                    alertDialog.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.cancel());
                    AlertDialog alert=alertDialog.create();
                    alert.show();
                } catch (Exception e) {
                    Log.e(TAG, "onReceive: -----------------------------ddd"+e.getMessage() );
                    e.printStackTrace();
                }
                }
    }};

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        Tools.reupdateResources(this);

         if (getIntent().getExtras()!=null){
             result = getIntent().getExtras().getString("result");
             if(result==null)
             {
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

        TabHost tabHost = getTabHost();
        TabHost.TabSpec homespec = tabHost.newTabSpec("Home");
        View tabIndicator1 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        ((ImageView) tabIndicator1.findViewById(R.id.icon)).setImageResource(R.drawable.homedrawable);
        ((TextView) tabIndicator1.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.home));
        TextView counter = ((TextView) tabIndicator1.findViewById(R.id.reqcount));
        homespec.setIndicator(tabIndicator1);

        Intent Intent1 = new Intent(this, HomeActivity.class).putExtra("result",result);
        homespec.setContent(Intent1);


        // Tab for OnSale
        TabHost.TabSpec onsalespec = tabHost.newTabSpec("Search");

        View tabIndicator2 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        ((ImageView) tabIndicator2.findViewById(R.id.icon)).setImageResource(R.drawable.paybilldrawable);
        ((TextView) tabIndicator2.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.paybill));


        counter_wallet = ((TextView) tabIndicator2.findViewById(R.id.reqcount));
        onsalespec.setIndicator(tabIndicator2);

        Intent intent2 = new Intent(this, PayBillAct.class);
        onsalespec.setContent(intent2);

        // Tab for Deals
        TabHost.TabSpec dealsspec = tabHost.newTabSpec("Chat");

        View tabIndicator3 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        ((ImageView) tabIndicator3.findViewById(R.id.icon)).setImageResource(R.drawable.activitydrawable);
        ((TextView) tabIndicator3.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.activity));
        counter_shedule = ((TextView) tabIndicator3.findViewById(R.id.reqcount));
        dealsspec.setIndicator(tabIndicator3);

        Intent intent3 = new Intent(this, TrasActivity.class);
        dealsspec.setContent(intent3);

        TabHost.TabSpec message = tabHost.newTabSpec("Chat");

        View tabIndicator6 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        ((ImageView) tabIndicator6.findViewById(R.id.icon)).setImageResource(R.drawable.messagedrawable);
        ((TextView) tabIndicator6.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.messages));
        counter_message = ((TextView) tabIndicator6.findViewById(R.id.reqcount));
        message.setIndicator(tabIndicator6);

        Intent intent6 = new Intent(this, MemberMessageAct.class);
        message.setContent(intent6);
        // Tab for Profile
        TabHost.TabSpec profilespec = tabHost.newTabSpec("Profile");

        View tabIndicator5 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        ((ImageView) tabIndicator5.findViewById(R.id.icon)).setImageResource(R.drawable.invitedrawable);
        ((TextView) tabIndicator5.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.invite));
        counter = ((TextView) tabIndicator5.findViewById(R.id.reqcount));
        profilespec.setIndicator(tabIndicator5);

        Intent Intent5 = new Intent(this, InviteActMain.class);
        profilespec.setContent(Intent5);

        try {
            tabHost.addTab(homespec);
            tabHost.addTab(onsalespec);
            tabHost.addTab(dealsspec);
            tabHost.addTab(message);
            tabHost.addTab(profilespec);

        } catch (Exception e) {
            e.printStackTrace();
        }

        tabHost.setCurrentTab(WhichIndex);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        if (scheduleTaskExecutor == null) {

        } else {
            scheduleTaskExecutor.shutdown();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (scheduleTaskExecutor == null) {

        } else {
            scheduleTaskExecutor.shutdown();
        }
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
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        int message_unseen_count = 0;
                        String counter_message_int = jsonObject.getString("unseen_count");


                        //impliment by sagar panse //

                      */
/*  String delete_status = jsonObject.getString("delete_status");

                        if (delete_status.equalsIgnoreCase("Deactive")) {
                            mySession.signinusers(false);
                            mySession.logoutUser();
                            Intent i = new Intent(MainTabActivity.this, AccountTypeSelectionAct.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                        }*//*


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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("MEMBER_HOME"));
        Tools.reupdateResources(this);

        try {
            scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    new MyCounterVal().execute();
                }
            }, 0, 8, TimeUnit.SECONDS);
        }catch (Exception e){
            Log.e(TAG, "onResume: "+e.getCause() );
        }
    }


    class GetVersionCode extends AsyncTask<Void, String, String> {
        //implimemnmt by sagar panse //
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + MainTabActivity.this.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
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
}*/
