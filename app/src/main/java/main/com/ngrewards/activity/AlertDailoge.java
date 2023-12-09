package main.com.ngrewards.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;

public class AlertDailoge extends AppCompatActivity {

    private SweetAlertDialog pDialog;
    private String reciept_url;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_alert_dailoge);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {

            reciept_url = bundle.getString("reciept_url");

        }

        SweatAlert();
    }

    private void SweatAlert() {

        pDialog = new SweetAlertDialog(AlertDailoge.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#042587"));
        pDialog.setTitleText(getString(R.string.your_order_successfully_placed));
        pDialog.setConfirmText(getString(R.string.ok));
        pDialog.setCancelable(false);
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent intent = new Intent(AlertDailoge.this, WebViewCalled.class);
                intent.putExtra("reciept_url", reciept_url);
                startActivity(intent);
                finish();
                pDialog.dismissWithAnimation();
            }
        });
        pDialog.show();
    }
}
