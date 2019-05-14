package com.redblack.taksim.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.redblack.taksim.R;

public class Yardim extends AppCompatActivity {

    private ImageButton btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yardim);

        btn_back = findViewById(R.id.bck_btn_yardim);
        btn_back.setOnClickListener((View) ->{

            finish();
        });
    }
}
