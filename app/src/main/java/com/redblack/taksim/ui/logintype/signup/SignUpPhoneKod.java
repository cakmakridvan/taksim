package com.redblack.taksim.ui.logintype.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.redblack.taksim.R;
import com.redblack.taksim.ui.logintype.login.LoginKod;
import com.redblack.taksim.ui.logintype.login.LoginPhone;
import com.redblack.taksim.ui.logintype.server.Server;
import com.redblack.taksim.utils.GpsUtils;
import com.redblack.taksim.utils.PreferenceLoginSession;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;

public class SignUpPhoneKod extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back,next;
    private ProgressDialog progressDialog;
    private Integer get_resultCode = 15;
    private Bundle extras;
    private String get_kod = "";
    private String get_number = "";
    private EditText entered_kod;
    private String get_enteredKod = "";
    private JSONObject jsonObject;
    private String get_jsonObject = "";
    private CustomerLoginm customerLoginm = null;
    private String get_token = "";
    private boolean isGPS = false;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_phone_kod);

        coordinatorLayout = findViewById(R.id.lyt_coordinator_signup_phonekod);

        //Initialize Paper
        Paper.init(SignUpPhoneKod.this);

        entered_kod = findViewById(R.id.edt_signup_phone_kod);

        jsonObject = new JSONObject();

     //get code and mhoneNumber form SignUpPhone
        extras = getIntent().getExtras();
        if(extras != null){
            get_kod = extras.getString("kod");
            get_number = extras.getString("mobilNo");
        }

        entered_kod.setText(get_kod);

        //Progress Diaolog initialize
        progressDialog = new ProgressDialog(SignUpPhoneKod.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setIndeterminate(true);

        back = findViewById(R.id.btn_bck_phone_kod);
        next = findViewById(R.id.btn_next_phone_kod);

        back.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_bck_phone_kod:
                finish();
                break;

            case R.id.btn_next_phone_kod:

                get_enteredKod = entered_kod.getText().toString();

                if(get_enteredKod.equals(get_kod)){
                    try {
                        //Create JsonObject to send WebService
                        jsonObject.put("mobile",get_number);
                        jsonObject.put("verifyCode",get_kod);
                        //JsonObject to String
                        get_jsonObject = jsonObject.toString();

                        customerLoginm = new CustomerLoginm(get_jsonObject);
                        customerLoginm.execute((Void) null);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Girilen kod hatalı", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(SignUpPhoneKod.this,R.color.colorAccent));
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }

                break;
        }

    }

    public class CustomerLoginm extends AsyncTask<Void, Void, Boolean> {

        private final String json;

        CustomerLoginm(String json){

            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.custom_progress);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                String getVerifyMobile_result = Server.CustomerLogin(json);
                if(!getVerifyMobile_result.trim().equalsIgnoreCase("false")){

                    try{

                        JSONObject jsonObject = new JSONObject(getVerifyMobile_result);
                        get_resultCode = jsonObject.getInt("errCode");
                        //String get_verifyMobile = jsonObject.getString("verifyCode");
                        Log.i("jsonObject",""+jsonObject);

                        //get Token
                        get_token = Server.token;
                        Log.i("token:","" + get_token);




                    }catch (JSONException e){
                        Log.i("Exception",e.getMessage());
                    }
                }else{
                    get_resultCode = 15;
                }

            }catch (Exception e){
                Log.i("Exception",e.getMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


            if(get_resultCode == 0){
                progressDialog.dismiss();

                //saved token Paper db
                Paper.book().write("token",get_token);

                //Save sharedPreferences of login to System
                new PreferenceLoginSession(SignUpPhoneKod.this).writePreference(get_token);

                Intent go_signup = new Intent(SignUpPhoneKod.this,SignUp.class);
                go_signup.putExtra("mobileNO",get_number);
                go_signup.putExtra("token",get_token);
                startActivity(go_signup);
            }

            else{
                progressDialog.dismiss();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, "İşlem Başarısız", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(SignUpPhoneKod.this,R.color.colorAccent));
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            }
        }

        @Override
        protected void onCancelled() {

            customerLoginm = null;
            progressDialog.dismiss();
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
