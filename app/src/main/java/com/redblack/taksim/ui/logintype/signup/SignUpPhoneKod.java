package com.redblack.taksim.ui.logintype.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.redblack.taksim.R;

public class SignUpPhoneKod extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back,next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_phone_kod);

        back = findViewById(R.id.btn_bck_phone_kod);
        next = findViewById(R.id.btn_next_phone_kod);

        back.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_bck_phone_kod:
                finish();
                break;

            case R.id.btn_next_phone_kod:
                startActivity(new Intent(this,SignUp.class));
                break;
        }

    }
}
