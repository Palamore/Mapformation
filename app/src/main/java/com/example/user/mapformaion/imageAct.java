package com.example.user.mapformaion;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class imageAct extends AppCompatActivity {
String iid;
android.widget.ImageView ImageView;



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview);

        ImageView = (android.widget.ImageView)findViewById(R.id.imageView);



 /*       Intent intent = getIntent();
        iid = intent.getStringExtra("image");



        ImageLoaderTask imageLoad = new ImageLoaderTask(
                ImageView,
                iid
        );
        imageLoad.execute();*/
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onBackPressed();


    }


}
