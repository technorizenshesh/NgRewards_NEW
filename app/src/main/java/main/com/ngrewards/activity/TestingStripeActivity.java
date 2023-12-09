package main.com.ngrewards.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;

public class TestingStripeActivity extends AppCompatActivity {
    String url = "http://connect.stripe.com/express/oauth/authorize?redirect_uri=https://myngrewards.com/demo/test-payment-page/&client_id=ca_DtpgsTKByfhAwa5P8oYfWT2CXNXaCnTq";
    //https://connect.stripe.com/express/oauth/authorize?response_type=code&client_id=ca_DtpgsTKByfhAwa5P8oYfWT2CXNXaCnTq&scope=read_write
    private TextView stripeaccount;
    private WebView stripewebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_stripe);
        stripeaccount = findViewById(R.id.stripeaccount);
        stripeaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "connect.stripe.com/express/oauth/authorize?redirect_uri=https://myngrewards.com/demo/wp-content/plugins/webservice/stripe_payment_form.php&client_id=ca_DtpgsTKByfhAwa5P8oYfWT2CXNXaCnTq&state=1";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://" + url));
                startActivity(i);
            }
        });
        idinit();
        clickevetn();
    }

    private void clickevetn() {
    }

    private void idinit() {
        stripewebview = (WebView) findViewById(R.id.stripewebview);


        stripewebview.getSettings().setJavaScriptEnabled(true);
        stripewebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        stripewebview.setWebViewClient(new Callback());
        //  String pdfURL = "http://mobileappdevelop.co/NAXCAN/about-us.html";
        String pdfURL = url;
        stripewebview.loadUrl(pdfURL);

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }

}