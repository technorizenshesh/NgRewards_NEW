package main.com.ngrewards.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;
import main.com.ngrewards.constant.BaseUrl;

public class VideoPlayActivity extends AppCompatActivity {

    private VideoView videoplayer;
    private RelativeLayout backlay;
    private String video_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        idinit();
        clickevent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.isEmpty()) {

        } else {
            video_url = bundle.getString("video_url");
            Log.e("TAG", "onCreate: -------------" + video_url);
            if (video_url != null && !video_url.equalsIgnoreCase("") && !video_url.equalsIgnoreCase(BaseUrl.video_baseurl)) {
                videoplayer.setVideoPath(video_url);
                videoplayer.start();
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

    private void idinit() {
        videoplayer = findViewById(R.id.videoplayer);
        backlay = findViewById(R.id.backlay);

    }
}
