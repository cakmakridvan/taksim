package com.redblack.taksim.ui.logintype.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.redblack.taksim.Main;
import com.redblack.taksim.R;
import com.redblack.taksim.model.User;
import com.redblack.taksim.ui.logintype.login.LoginPhone;
import com.redblack.taksim.ui.logintype.server.Server;
import com.redblack.taksim.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back,complete;
    private EditText name,lastName,eMail;
    private JSONObject jsonObject;
    private JSONObject jsonObject_info;
    private JSONObject jsonObject_info_baslik;
    private String get_jsonObject = "";
    private ProgressDialog progressDialog;
    private Bundle extras;
    private String get_mobilNo = "";
    private String getName = "";
    private String getLastName = "";
    private String getMail = "";
    private String get_token = "";
    private UpdateCustomer updateCustomer = null;
    private Integer get_resultCode = 15; //default value
    private CoordinatorLayout coordinatorLayout;
    private String getNickName = "", getMailAddress = "";
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
     //Paper initialize
        Paper.init(SignUp.this);

        //get code and mhoneNumber form SignUpPhone
        extras = getIntent().getExtras();
        if(extras != null){

            get_mobilNo = extras.getString("mobileNO");
            get_token = extras.getString("token");
        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout_signup);

        //Progress Diaolog initialize
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setIndeterminate(true);

        name = findViewById(R.id.edt_signup_name);
        lastName = findViewById(R.id.edt_signup_lastname);
        eMail = findViewById(R.id.edt_signup_mail);

        back = findViewById(R.id.btn_bck_signup);
        complete = findViewById(R.id.btn_signup_complete);

        back.setOnClickListener(this);
        complete.setOnClickListener(this);

        jsonObject = new JSONObject();
        jsonObject_info = new JSONObject();
        jsonObject_info_baslik = new JSONObject();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_bck_signup:
                finish();
                break;

            case R.id.btn_signup_complete:

                getName = name.getText().toString();
                getLastName = lastName.getText().toString();
                getMail = eMail.getText().toString();

                if(TextUtils.isEmpty(getName) || TextUtils.isEmpty(getLastName) || TextUtils.isEmpty(getMail)){

                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Bilgileri Kontrol Ediniz.", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(SignUp.this,R.color.colorAccent));
                    snackbar.show();

                }else if(!Utility.isValidEmail(getMail)){

                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Mail Adresinizi Kontrol Ediniz.", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(SignUp.this,R.color.colorAccent));
                    snackbar.show();
                }

                else {

                    try {
                        //Create JsonObject to send WebService
                        jsonObject.put("mobile", get_mobilNo);

                        jsonObject_info.put("age", 25);
                        jsonObject_info.put("compAddrName", "");
                        jsonObject_info.put("compLat", 0.0);
                        jsonObject_info.put("compLon", 0.0);
                        jsonObject_info.put("compName", "");
                        jsonObject_info.put("email", getMail);
                        jsonObject_info.put("gender", 1);
                        jsonObject_info.put("guarderPhone", "");
                        jsonObject_info.put("homeAddrName", "");
                        jsonObject_info.put("homeLat", 0.0);
                        jsonObject_info.put("homeLon", 0.0);
                        jsonObject_info.put("level", 0);
                        jsonObject_info.put("nickName", getName + getLastName);
                        jsonObject_info.put("occupation", "");
                        jsonObject_info.put("photo", "");
                        jsonObject_info.put("trade", "");
                        //jsonObject_info.put("")

                        jsonObject.put("customerInfo", jsonObject_info);

                        //JsonObject to String
                        get_jsonObject = jsonObject.toString();
                        Log.i("get_jsonObject", "" + get_jsonObject);

                        //Send to Service
                        updateCustomer = new UpdateCustomer(get_jsonObject,get_token);
                        updateCustomer.execute((Void) null);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public class UpdateCustomer extends AsyncTask<Void, Void, Boolean> {

        private final String json;
        private final String token;

        UpdateCustomer(String json, String token){

            this.json = json;
            this.token = token;
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
                String getVerifyCode_result = Server.UpdateCustomerInfo(json,token);
                if(!getVerifyCode_result.trim().equalsIgnoreCase("false")){

                    try{
                        JSONObject jsonObject = new JSONObject(getVerifyCode_result);
                        get_resultCode = jsonObject.getInt("errCode");
                        String getCustomerInfo = jsonObject.getString("customerInfo");
                        JSONObject jsonObject_customerInfo = new JSONObject(getCustomerInfo);
                        getNickName = jsonObject_customerInfo.getString("nickName");
                        getMailAddress = jsonObject_customerInfo.getString("email");
                        Log.i("resultCode",""+get_resultCode);
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
            progressDialog.dismiss();


            if(get_resultCode == 0){
                progressDialog.dismiss();

                Intent go_MainPage = new Intent(SignUp.this,Main.class);
                go_MainPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(go_MainPage);

             //Save Paper Profil info of User
                user = new User(getNickName,getMailAddress,get_mobilNo);
                Paper.book().write("user_info",user);

            }

            else{
                progressDialog.dismiss();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, "İşlem Başarısız", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(SignUp.this,R.color.colorAccent));
                snackbar.show();

            }
        }

        @Override
        protected void onCancelled() {

            updateCustomer = null;
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
