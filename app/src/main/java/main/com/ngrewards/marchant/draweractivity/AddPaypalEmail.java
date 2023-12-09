package main.com.ngrewards.marchant.draweractivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;

public class AddPaypalEmail extends AppCompatActivity {

    private TextView addpaypal;
    private RelativeLayout backlay;
    private EditText paypal_email_et;
    private String paypal_email_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_paypal_email);
        idinit();
        clickevent();
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addpaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paypal_email_str = paypal_email_et.getText().toString();
                if (paypal_email_str == null || paypal_email_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddPaypalEmail.this, getResources().getString(R.string.enterpaypalemail), Toast.LENGTH_LONG).show();
                } else {

                }
            }
        });
    }

    private void idinit() {
        addpaypal = findViewById(R.id.addpaypal);
        backlay = findViewById(R.id.backlay);
        paypal_email_et = findViewById(R.id.paypal_email_et);
    }


}
