package com.redblack.taksim.ui.logintype.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.redblack.taksim.R;

public class LoginKod extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back,complete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_kod);

        back = findViewById(R.id.btn_bck_login_code);
        complete = findViewById(R.id.btn_next_login_code);

        back.setOnClickListener(this);
        complete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_bck_login_code:
                finish();
                break;

            case R.id.btn_next_login_code:

                break;
        }
    }
}
