package main.com.ngrewards.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;

public class SeeDocumentUrl extends AppCompatActivity {

    private WebView webview;
    private RelativeLayout backlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_document_url);
        idint();
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

    private void idint() {
        webview = findViewById(R.id.webview);
        backlay = findViewById(R.id.backlay);
    }
}
