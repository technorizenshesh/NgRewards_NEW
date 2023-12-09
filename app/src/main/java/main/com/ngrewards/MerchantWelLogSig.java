package main.com.ngrewards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.marchant.MarchantLogin;
import main.com.ngrewards.marchant.activity.MerchantSignupSlider;


public class MerchantWelLogSig extends AppCompatActivity {
    private TextView login_tv, createaccount;
    private RelativeLayout backlay;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_merchant_wel_log_sig);
        idinti();
        clickevent();
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MerchantWelLogSig.this, MarchantLogin.class);
                startActivity(i);
            }
        });
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MerchantWelLogSig.this, MerchantSignupSlider.class);
                startActivity(i);
            }
        });
    }

    private void idinti() {
        backlay = findViewById(R.id.backlay);
        login_tv = findViewById(R.id.login_tv);
        createaccount = findViewById(R.id.createaccount);
    }
}
