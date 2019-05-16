package com.redblack.taksim.ui.logintype.login;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.redblack.taksim.Main;
import com.redblack.taksim.R;
import com.redblack.taksim.ui.logintype.server.Server;
import com.redblack.taksim.ui.logintype.signup.SignUp;
import com.redblack.taksim.ui.logintype.signup.SignUpPhoneKod;
import com.redblack.taksim.ui.viewpager.ViewPager;
import com.redblack.taksim.utils.PreferenceLoginSession;
import com.redblack.taksim.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;

public class LoginKod extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back,complete;
    private Bundle extras;
    private String get_kod = "";
    private ProgressDialog progressDialog;
    private Integer get_resultCode = 15; //ResultCode error message
    private String get_token = "";
    private String get_mobilNo = "";
    private JSONObject jsonObject;
    private String get_jsonObject = "";
    private CustomerLoginm customerLoginm = null;
    private EditText edt_kod;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_kod);

        //Paper initialize
        Paper.init(LoginKod.this);

        coordinatorLayout = findViewById(R.id.lyt_coordinator_login_kod);

        //Progress Diaolog initialize
        progressDialog = new ProgressDialog(LoginKod.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setIndeterminate(true);

        jsonObject = new JSONObject();
        edt_kod = findViewById(R.id.edt_login_kod);

        //get code and mhoneNumber form SignUpPhone
        extras = getIntent().getExtras();
        if(extras != null){

            get_kod = extras.getString("kod");
            get_mobilNo = extras.getString("mobilNo");
        }

        edt_kod.setText(get_kod);

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

                try {
                    //Create JsonObject to send WebService
                    jsonObject.put("mobile",get_mobilNo);
                    jsonObject.put("verifyCode",get_kod);
                    //JsonObject to String
                    get_jsonObject = jsonObject.toString();

                 //Send Service
                    customerLoginm = new CustomerLoginm(get_jsonObject);
                    customerLoginm.execute((Void) null);


                } catch (JSONException e) {
                    e.printStackTrace();
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

                        Log.i("GetresultCode:","" + get_resultCode);

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
                new PreferenceLoginSession(LoginKod.this).writePreference(get_token);

                Intent go_mainPage = new Intent(LoginKod.this,Main.class);
                go_mainPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(go_mainPage);
            }

            else{
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "İşlem Başarısız", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(LoginKod.this,R.color.colorAccent));
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
