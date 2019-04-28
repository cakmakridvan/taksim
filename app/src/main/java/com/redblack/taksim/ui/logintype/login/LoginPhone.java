package com.redblack.taksim.ui.logintype.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.redblack.taksim.R;

public class LoginPhone extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back,next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_phone);

        back = findViewById(R.id.btn_bck_login_phone);
        next = findViewById(R.id.btn_next_login_phone);

        back.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_bck_login_phone:
                finish();
                break;

            case R.id.btn_next_login_phone:
                startActivity(new Intent(this,LoginKod.class));
                break;
        }
    }
}
