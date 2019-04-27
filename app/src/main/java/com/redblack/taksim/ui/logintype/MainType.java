package com.redblack.taksim.ui.logintype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.redblack.taksim.R;
import com.redblack.taksim.ui.logintype.signup.SignUpPhone;

public class MainType extends AppCompatActivity implements View.OnClickListener {

    private ImageButton signup,login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_type);

        signup = findViewById(R.id.btn_signup);
        login = findViewById(R.id.btn_login);

        signup.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_signup:
                startActivity(new Intent(this,SignUpPhone.class));
                break;

            case R.id.btn_login:
                break;
        }

    }
}
