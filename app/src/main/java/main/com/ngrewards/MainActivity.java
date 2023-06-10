package main.com.ngrewards;

import android.os.Bundle;
import android.widget.FrameLayout;

import main.com.ngrewards.draweractivity.BaseActivity;

public class MainActivity extends BaseActivity {

    FrameLayout contentFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);

    }
}
