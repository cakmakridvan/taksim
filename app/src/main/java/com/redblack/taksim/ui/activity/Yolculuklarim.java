package com.redblack.taksim.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.redblack.taksim.R;

public class Yolculuklarim extends AppCompatActivity {

    ImageButton btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yolculuklarim);

        btn_back = findViewById(R.id.bck_btn_yolculuklarim);
        btn_back.setOnClickListener((View) ->{
            finish();
        });
    }
}
