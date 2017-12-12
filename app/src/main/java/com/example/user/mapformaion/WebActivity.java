package com.example.user.mapformaion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class WebActivity extends AppCompatActivity {

    String gUrl;
    String marker_str;
    String gCnt;
    String nCnt;
    String wCnt;
    int cnt_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        gUrl = intent.getStringExtra("Param1");
        gCnt = intent.getStringExtra("Param2");
        marker_str = intent.getStringExtra("Param3");
        cnt_value = Integer.parseInt(intent.getStringExtra("Param4"));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Locations");
        Query locate = myRef.child(marker_str).orderByChild(gCnt).limitToFirst(10);
        locate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Locations");
                myRef.child(marker_str).child(gCnt).setValue(cnt_value + 1);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        WebView wv = (WebView)findViewById(R.id.web);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        wv.loadUrl(gUrl);

   //     finish();
    }
}
