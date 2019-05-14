package com.redblack.taksim.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.redblack.taksim.R;

public class Adreslerim extends AppCompatActivity {

    private ImageButton btn_bck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adreslerim);

        btn_bck = findViewById(R.id.bck_btn_adreslerim);
        btn_bck.setOnClickListener((View) ->{

            finish();
        });
    }
}
