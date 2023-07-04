package main.com.ngrewards.marchant.merchantbottum;

import static main.com.ngrewards.marchant.draweractivity.MerchantBaseActivity.reqcounft;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import main.com.ngrewards.BuildConfig;
import main.com.ngrewards.R;
import main.com.ngrewards.activity.AccountTypeSelectionAct;
import main.com.ngrewards.activity.app.Config;
import main.com.ngrewards.beanclasses.GalleryBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;

public class MerchantBottumAct extends TabActivity {

    int WhichIndex = 0;
    private Boolean exit;
    String scrsts = "";
    MySession mySession;
    public static String user_log_data = "", user_id = "", notification_data = "";
    TextView counter_wallet, counter_message;
    ScheduledExecutorService scheduleTaskExecutor;
    String currentVersion = "";
    private Dialog canceldialog;
    public static ArrayList<GalleryBean> ImagePathArrayList;

    public final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
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
                reqcounft.setVisibility(View.GONE);
                new GetProfile().execute();
            }
        }
    };

    private class GetProfile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ImagePathArrayList = new ArrayList<>();
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
//https://international.myngrewards.com/wp-content/plugins/webservice/merchant_profile.php?merchant_id=332
            try {
                String postReceiverUrl = BaseUrl.baseurl + "merchant_profile.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("merchant_id", user_id);
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
                Log.e("GetProfile Response", ">>>>>>>>>>>>" + response);
                return response;
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
                   // Log.e("TAG", "JSONObjectJSONObjectJSONObjectJSONObject: "+jsonObject.toString() );
                    String message = jsonObject.getString("status");
                    reqcounft.setVisibility(View.GONE);
                    if (message.equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String unseen_count = jsonObject1.getString("unseen_count");
                        String admin_created_password    = jsonObject1.getString("admin_created_password");
                        String sell_items_reomve_access = jsonObject1.getString("sell_items_reomve_access");
                        Log.e("TAG", "JSONObjectJSONObjectJSONObjectJSONObject: sell_items_reomve_access"+sell_items_reomve_access );
                        Log.e("TAG", "JSONObjectJSONObjectJSONObjectJSONObject:admin_created_password "+admin_created_password );
mySession.setsell_items_reomve_access(sell_items_reomve_access);
mySession.setadmin_created_password(admin_created_password);
                        mySession.setPassSet("");

                        if (unseen_count.equals("0")) {
                            reqcounft.setVisibility(View.GONE);
                        } else {
                            reqcounft.setVisibility(View.VISIBLE);
                            reqcounft.setText("" + unseen_count);
                        }

                        Log.e("unseen_count>>>", unseen_count);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(broadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));

        setContentView(R.layout.activity_main_tab);
        ImagePathArrayList = new ArrayList<>();
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

            } catch (PackageManager.NameNotFoundException e) {
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
            Log.e("Get Notification >>", "" + message);
            if (message == null || message.equalsIgnoreCase("") ||
                    message.equalsIgnoreCase("null")) {

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
                if (scrsts.equalsIgnoreCase("shedule")) {
                    WhichIndex = extra.getInt("WhichIndex", 3);
                } else if (scrsts.equalsIgnoreCase("wallet")) {
                    WhichIndex = extra.getInt("WhichIndex", 2);
                }
                if (extra.containsKey("WhichIndex")) {

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
        Intent Intent1 = new Intent(this, MerHomeActivity.class);
        homespec.setContent(Intent1);


        // Tab for OnSale
        TabHost.TabSpec onsalespec = tabHost.newTabSpec("Search");
        View tabIndicator2 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        ((ImageView) tabIndicator2.findViewById(R.id.icon)).setImageResource(R.drawable.statedrawable);
        ((TextView) tabIndicator2.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.states));
        counter_wallet = ((TextView) tabIndicator2.findViewById(R.id.reqcount));
        onsalespec.setIndicator(tabIndicator2);

        Intent intent2 = new Intent(this, MerStatusAct.class);
        onsalespec.setContent(intent2);


        // Tab for Deals
        TabHost.TabSpec dealsspec = tabHost.newTabSpec("Chat");

        View tabIndicator3 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        ((ImageView) tabIndicator3.findViewById(R.id.icon)).setImageResource(R.drawable.messagedrawable);
        ((TextView) tabIndicator3.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.messages));
        counter_message = ((TextView) tabIndicator3.findViewById(R.id.reqcount));
        dealsspec.setIndicator(tabIndicator3);

        Intent intent3 = new Intent(this, MerMessageAct.class);
        dealsspec.setContent(intent3);


        // Tab for Profile
        TabHost.TabSpec profilespec = tabHost.newTabSpec("Profile");

        View tabIndicator5 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        ((ImageView) tabIndicator5.findViewById(R.id.icon)).setImageResource(R.drawable.selldrawable);
        ((TextView) tabIndicator5.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.sell));
        counter = ((TextView) tabIndicator5.findViewById(R.id.reqcount));
        profilespec.setIndicator(tabIndicator5);

        Intent Intent5 = new Intent(this, MerchantBotSell.class);
        profilespec.setContent(Intent5);

        // Adding all TabSpec to TabHost
        tabHost.addTab(homespec);
        tabHost.addTab(onsalespec);
        tabHost.addTab(dealsspec);
        tabHost.addTab(profilespec);
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

    @Override
    protected void onResume() {
        super.onResume();
        new GetProfile().execute();

        registerReceiver(broadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new MyCounterVal().execute();
            }
        }, 0, 8, TimeUnit.SECONDS);
    }

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
                Log.e("Tab user_id >. ", " >>" + user_id);
                params.put("type", "Merchant");
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
                        String delete_status = jsonObject.getString("delete_status");
                        if (delete_status.equalsIgnoreCase("Deactive")) {
                            mySession.signinusers(false);
                            mySession.logoutUser();
                            Intent i = new Intent(MerchantBottumAct.this, AccountTypeSelectionAct.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                        }
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

    class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + MerchantBottumAct.this.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();

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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (currentVersion != null) {
                    if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                        appUpdate();
                    }

                }

            }

        }
    }

    private void appUpdate() {
        canceldialog = new Dialog(MerchantBottumAct.this);
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
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
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

}