package com.redblack.taksim.ui.logintype.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.redblack.taksim.R;
import com.redblack.taksim.utils.Utility;

public class SignUpPhone extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btn_back,btn_next;
    private EditText number;
    private String getNumber = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_phone);

        btn_back = findViewById(R.id.btn_bck_phone);
        btn_next = findViewById(R.id.btn_next_phone);
        number = findViewById(R.id.edt_signup_phone);

        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_bck_phone:
                finish();
                break;

            case R.id.btn_next_phone:

                getNumber = number.getText().toString();

                if(!Utility.isValidMobile(getNumber.length())){

                    Toast.makeText(this,"Numaranızı Kontrol Edin",Toast.LENGTH_LONG).show();
                }

                else{

                    startActivity(new Intent(this,SignUpPhoneKod.class));
                }

                break;
        }

    }
}
