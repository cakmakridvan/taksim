package com.redblack.taksim.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.redblack.taksim.Main;
import com.redblack.taksim.R;
import com.redblack.taksim.viewpager.ViewPager;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent go_main = new Intent(Splash.this,ViewPager.class);
                startActivity(go_main);

                //close this activity
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
