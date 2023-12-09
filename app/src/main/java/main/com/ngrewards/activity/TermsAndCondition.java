package main.com.ngrewards.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;

public class TermsAndCondition extends AppCompatActivity {

    boolean loadingFinished = true;
    boolean redirect = false;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_called);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && !bundle.isEmpty()) {

            status = bundle.getString("status");
        }

        SetupUI();
    }

    private void SetupUI() {

        WebView w = (WebView) findViewById(R.id.webview123);

        WebSettings webSettingDimrah = w.getSettings();
        webSettingDimrah.setJavaScriptEnabled(true);

        w.getSettings().setLoadsImagesAutomatically(true);
        w.getSettings().setJavaScriptEnabled(true);
        w.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        w.getSettings().setDomStorageEnabled(true);
        w.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        w.setWebViewClient(new HelloWebViewClient());
        w.getSettings().setLoadsImagesAutomatically(true);
        w.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        w.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            //TermsWV.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        if (status.equals("privacy")) {

            w.loadUrl("http://myngrewards.com/privacy-policy/?v=7516fd43adaa");
        } else {

            w.loadUrl("https://myngrewards.com/terms-and-conditions/");
        }


        w.getSettings().setJavaScriptEnabled(true);

        w.setWebViewClient(new WebViewClient());


        w.setWebViewClient(new WebViewClient() {
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
