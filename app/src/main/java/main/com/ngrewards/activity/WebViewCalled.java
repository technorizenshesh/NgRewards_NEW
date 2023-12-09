package main.com.ngrewards.activity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;
import main.com.ngrewards.androidmigx.MainTabActivity;

public class WebViewCalled extends AppCompatActivity {

    private final String postUrl = "https://api.androidhive.info/webview/index.html";
    private String reciept_url;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_called);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            reciept_url = bundle.getString("reciept_url");
        }
        SetupUI();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(this, MainTabActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
        PreferenceConnector.writeString(WebViewCalled.this, PreferenceConnector.Logout_Status, "false");
    }

    private void SetupUI() {

        WebView webView = (WebView) findViewById(R.id.webview123);
        WebSettings webSettingDimrah = webView.getSettings();
        webSettingDimrah.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(reciept_url);
        webView.setHorizontalScrollBarEnabled(false);

    }
}
