package com.redblack.taksim.ui.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.redblack.taksim.R;
import com.redblack.taksim.model.User;

import io.paperdb.Paper;

public class Profil extends AppCompatActivity {

    private User user;
    private String getName = "", getMail = "", getNumber = "";
    private EditText name,lastName,phone,mail;
    private ImageButton btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

        name = findViewById(R.id.edt_profil_name);
        lastName = findViewById(R.id.edt_profil_lastName);
        phone = findViewById(R.id.edt_profil_phoneNumber);
        mail = findViewById(R.id.edt_profil_mail);

        btn_back = findViewById(R.id.bck_btn_profil);
        btn_back.setOnClickListener((View) ->{

            finish();
        });

     //Get User information
        Paper.init(Profil.this);
        user = Paper.book().read("user_info");
        if(user != null){
            getName = user.getNameUser();
            getMail = user.getMailUser();
            getNumber = user.getMobilNoUser();

            name.setText(getName);
            mail.setText(getMail);
            phone.setText(getNumber);
        }

    }

    //Motion Gesture for remove keyboard
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
