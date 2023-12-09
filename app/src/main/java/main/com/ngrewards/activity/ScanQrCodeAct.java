package main.com.ngrewards.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;

public class ScanQrCodeAct extends AppCompatActivity {

    private RelativeLayout backlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
        idints();
        clciekvent();
    }

    private void clciekvent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idints() {
        backlay = findViewById(R.id.backlay);
    }
}
