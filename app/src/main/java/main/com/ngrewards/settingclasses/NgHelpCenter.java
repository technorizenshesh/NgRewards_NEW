package main.com.ngrewards.settingclasses;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;
import main.com.ngrewards.constant.BaseUrl;

public class NgHelpCenter extends AppCompatActivity {

    private RelativeLayout backlay;
    private WebView helpwebview;
    private ProgressBar progressbar;
    private boolean sts = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ng_help_center);
        idint();
        clickevent();
        helpwebview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                progressbar.setVisibility(View.VISIBLE);
                Log.e("DDDD", "lll " + urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                progressbar.setVisibility(View.VISIBLE);
                Log.e("DDDD", "ssss " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressbar.setVisibility(View.GONE);
                Log.e("DDDD", "eee " + url);
                if (sts) {
                    helpwebview.loadUrl(url);
                    sts = false;
                }

            }
        });

    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idint() {
        backlay = findViewById(R.id.backlay);
        progressbar = findViewById(R.id.progressbar);
        helpwebview = findViewById(R.id.helpwebview);
       /* helpwebview.getSettings().setJavaScriptEnabled(true);
        helpwebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        helpwebview.setWebViewClient(new Callback());*/
        try {
            helpwebview.setWebViewClient(new Callback());
            helpwebview.getSettings().setDomStorageEnabled(true);
            helpwebview.getSettings().setLoadsImagesAutomatically(true);
            helpwebview.getSettings().setJavaScriptEnabled(true);
            helpwebview.getSettings().setDomStorageEnabled(true);
            helpwebview.getSettings().setUseWideViewPort(true);
            helpwebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            helpwebview.loadUrl(BaseUrl.helpurl);

        } catch (Exception e) {
            e.printStackTrace();
        }


       /* String pdfURL = BaseUrl.helpurl;
        helpwebview.loadUrl(pdfURL);*/
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (true);
        }
    }

}
