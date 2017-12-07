package com.example.user.mapformaion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    String gUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        gUrl = intent.getStringExtra("Param1");

        WebView wv = (WebView)findViewById(R.id.web);
        wv.loadUrl(gUrl);

        finish();
    }
}
