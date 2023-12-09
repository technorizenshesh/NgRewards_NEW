package main.com.ngrewards.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;

public class TakePermissionAct extends AppCompatActivity {

    private CheckBox locationchaeeck, cameras, contactss;
    private TextView start_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_permission);
        idinit();
        start_tv = findViewById(R.id.start_tv);
        start_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactss.isChecked() && cameras.isChecked() && locationchaeeck.isChecked()) {
                    Intent i = new Intent(TakePermissionAct.this, AccountTypeSelectionAct.class);
                    startActivity(i);
                } else {
                    Toast.makeText(TakePermissionAct.this, getResources().getString(R.string.needtoallow), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void idinit() {
        locationchaeeck = findViewById(R.id.locationchaeeck);
        cameras = findViewById(R.id.cameras);
        contactss = findViewById(R.id.contactss);
    }
}
