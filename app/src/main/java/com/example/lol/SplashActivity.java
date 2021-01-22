package com.example.lol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView mImage;
    TextView logo, tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom);

        mImage=findViewById(R.id.imageView);
        logo=findViewById(R.id.textView);
        tag=findViewById(R.id.textView2);

        mImage.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        tag.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3900);
    }
}