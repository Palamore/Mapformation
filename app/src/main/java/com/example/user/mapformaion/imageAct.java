package com.example.user.mapformaion;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


public class imageAct extends AppCompatActivity {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setBackgroundResource(R.drawable.frame_anim);
        AnimationDrawable countdownAnim = (AnimationDrawable) imageView.getBackground();
        countdownAnim.start();


    new Thread(){
        @Override
        public void run(){
            try {
                Thread.sleep(2000);
            finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }.start();

    }


}
