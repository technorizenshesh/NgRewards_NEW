package main.com.ngrewards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import main.com.ngrewards.activity.LoginActivity;
import main.com.ngrewards.activity.SliderActivity;

public class MemberWelLogSignAct extends AppCompatActivity {
    private TextView login_tv,createaccount;

    private RelativeLayout backlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_wel_log_sign);
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

                Intent i = new Intent(MemberWelLogSignAct.this, LoginActivity.class);
                startActivity(i);
            }
        });createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MemberWelLogSignAct.this,SliderActivity.class);
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
