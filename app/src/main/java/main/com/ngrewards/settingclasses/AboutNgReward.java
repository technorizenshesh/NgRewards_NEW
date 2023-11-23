package main.com.ngrewards.settingclasses;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import main.com.ngrewards.R;

public class AboutNgReward extends AppCompatActivity {

    private RelativeLayout backlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_ng_reward);
        idiint();
        clickevent();
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idiint() {
        backlay = findViewById(R.id.backlay);
    }
}
