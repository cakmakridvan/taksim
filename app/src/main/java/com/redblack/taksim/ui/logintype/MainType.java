package com.redblack.taksim.ui.logintype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.redblack.taksim.Main;
import com.redblack.taksim.R;
import com.redblack.taksim.ui.logintype.login.LoginPhone;
import com.redblack.taksim.ui.logintype.signup.SignUpPhone;
import com.redblack.taksim.ui.viewpager.ViewPager;
import com.redblack.taksim.utils.PreferenceLoginSession;
import com.redblack.taksim.utils.PreferenceManager;

public class MainType extends AppCompatActivity implements View.OnClickListener {

    private ImageButton signup,login;
    private String getSavedToken = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //Check of Logined System or Logout System
        if(new PreferenceLoginSession(MainType.this).checkPreference()){ //it should be

            getSavedToken = new PreferenceLoginSession(MainType.this).getToken();
            Log.i("getSavedToken","" + getSavedToken);
            startActivity(new Intent(MainType.this,Main.class));
            finish();
        }

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
                startActivity(new Intent(this,LoginPhone.class));
                break;
        }

    }
}
