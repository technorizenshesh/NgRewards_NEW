package main.com.ngrewards.activity.memberstripe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.app.Config;
import main.com.ngrewards.activity.app.NotificationUtils;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;

public class MemberStripeExpressAcountAct extends AppCompatActivity {

    ScheduledExecutorService scheduleTaskExecutor;
    boolean loadingFinished = true;
    boolean redirect = false;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    private RelativeLayout backlay;
    private WebView stripewebview;
    private MySession mySession;
    private String user_id = "";
    private ProgressBar progressabar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_express_acount);

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

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Log.e("Push_STRIPE: ", "" + message);
                    JSONObject data = null;
                    try {
                        data = new JSONObject(message);
                        String keyMessage = data.getString("key").trim();
                        if (keyMessage.equalsIgnoreCase("Your Stripe Connect Account Created Successfully")) {
                            Toast.makeText(MemberStripeExpressAcountAct.this, getResources().getString(R.string.youraccountcreatedsucc), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        idinit();
        clickevent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        scheduleTaskExecutor.shutdown();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());

        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

            }
        }, 10, 20, TimeUnit.SECONDS);
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idinit() {
        progressabar = findViewById(R.id.progressabar);
        backlay = findViewById(R.id.backlay);
        stripewebview = findViewById(R.id.stripewebview);
        stripewebview.clearCache(true);

        String stripeurl = BaseUrl.STRIPE_OAUTH_URL_MEMBER + user_id;
        stripewebview.getSettings().setLoadsImagesAutomatically(true);
        stripewebview.getSettings().setJavaScriptEnabled(true);
        stripewebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        stripewebview.setWebViewClient(new HelloWebViewClient());
        stripewebview.getSettings().setDomStorageEnabled(true);
        //  stripewebview.getSettings().setAppCacheEnabled(false);
        stripewebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        stripewebview.getSettings().setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stripewebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        stripewebview.loadUrl(stripeurl);

        stripewebview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }
                progressabar.setVisibility(View.VISIBLE);
                loadingFinished = false;
                view.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                progressabar.setVisibility(View.VISIBLE);
//SHOW LOADING IF IT ISNT ALREADY VISIBLE
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressabar.setVisibility(View.GONE);
                if (!redirect) {
                    loadingFinished = true;
                }

                if (loadingFinished && !redirect) {
//HIDE LOADING IT HAS FINISHED

                } else {
                    redirect = false;
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        scheduleTaskExecutor.shutdown();
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }


}
