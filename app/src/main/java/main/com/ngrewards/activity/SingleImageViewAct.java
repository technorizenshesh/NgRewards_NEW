package main.com.ngrewards.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import main.com.ngrewards.R;

public class SingleImageViewAct extends AppCompatActivity {

    private ImageView fullimage;
    private RelativeLayout backlay;
    private String image_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image_view);
        idinti();
        clickevent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.isEmpty()) {

        } else {
            image_str = bundle.getString("image_str");
            if (image_str != null) {
                Glide.with(SingleImageViewAct.this).load(image_str).into(fullimage);

            }

        }

    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idinti() {
        fullimage = findViewById(R.id.fullimage);
        backlay = findViewById(R.id.backlay);
    }
}
