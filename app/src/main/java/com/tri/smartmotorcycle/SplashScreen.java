package com.tri.smartmotorcycle;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    ImageView LogoSplashGps, LogoSplash;
    TextView textJudul, textDeskripsi, textSubDeskripsi;
    Animation Top_Animation, Bottom_Animation, Left_Animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        LogoSplash = findViewById(R.id.logo_splash);
        LogoSplashGps = findViewById(R.id.logo_satelit);
        textJudul = findViewById(R.id.textutama);
        textDeskripsi = findViewById(R.id.text_deskripsi);
        textSubDeskripsi = findViewById(R.id.text_subdeskripsi);

        Top_Animation = AnimationUtils.loadAnimation(this, R.anim.topanim);
        Left_Animation = AnimationUtils.loadAnimation(this, R.anim.leftanim);
        Bottom_Animation = AnimationUtils.loadAnimation(this, R.anim.bottomanim);

        LogoSplash.setAnimation(Top_Animation);
        LogoSplashGps.setAnimation(Left_Animation);
        textJudul.setAnimation(Bottom_Animation);
        textDeskripsi.setAnimation(Bottom_Animation);
        textSubDeskripsi.setAnimation(Bottom_Animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);    }

}
