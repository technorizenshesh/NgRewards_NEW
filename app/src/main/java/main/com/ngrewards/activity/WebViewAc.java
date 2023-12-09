package main.com.ngrewards.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import ir.alirezabdn.wp7progress.WP10ProgressBar;
import main.com.ngrewards.R;

public class WebViewAc extends AppCompatActivity {

    boolean loadingFinished = true;
    boolean redirect = false;
    private WebView TermsWV;
    private String Url;
    private WP10ProgressBar loader_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        TermsWV = (WebView) findViewById(R.id.TermsWV3);
        loader_page = findViewById(R.id.loader_page);
        loader_page.showProgressBar();

        Random addition1 = new Random();
        int additionint1 = addition1.nextInt(100) + 1;
        String random_no = String.valueOf(additionint1);

        Url = "https://myngrewards.com/signup.php?affiliate_name=ngrewardsllc&affiliate_no=" + random_no + "&how_invited_you=";

        TermsWV.getSettings().setLoadsImagesAutomatically(true);
        TermsWV.getSettings().setJavaScriptEnabled(true);
        TermsWV.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        TermsWV.setWebViewClient(new HelloWebViewClient());
        TermsWV.getSettings().setDomStorageEnabled(true);
        TermsWV.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        TermsWV.getSettings().setLoadsImagesAutomatically(true);
        TermsWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        TermsWV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TermsWV.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        Log.e("signup_url>>>", Url);
        TermsWV.loadUrl(Url);
        TermsWV.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                view.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!redirect) {
                    loadingFinished = true;
                }
                if (loadingFinished && !redirect) {
                    loader_page.hideProgressBar();
                } else {
                    redirect = false;
                }

            }
        });
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}