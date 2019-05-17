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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.redblack.taksim.R;
import com.redblack.taksim.ui.logintype.login.LoginPhone;
import com.redblack.taksim.ui.logintype.server.Server;
import com.redblack.taksim.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpPhone extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btn_back,btn_next;
    private EditText number;
    private String getNumber = "";
    private ProgressDialog progressDialog;
    private Integer get_resultCode = 15;
    private VerifyCode verifyCode = null;
    private JSONObject jsonObject;
    private String get_jsonObject = "";
    private String numberPhone = "";
    private String get_verifyCode = "";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_phone);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout_signup_phone);

        //Progress Diaolog initialize
        progressDialog = new ProgressDialog(SignUpPhone.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setIndeterminate(true);

        btn_back = findViewById(R.id.btn_bck_phone);
        btn_next = findViewById(R.id.btn_next_phone);
        number = findViewById(R.id.edt_signup_phone);

        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        jsonObject = new JSONObject();

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

                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Numaranızı Kontrol Edin", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(SignUpPhone.this,R.color.colorAccent));
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();

                }

                else{

                    numberPhone = "+90" + getNumber;

                    try {
                    //Create JsonObject to send WebService
                        jsonObject.put("mobile",numberPhone);
                        jsonObject.put("debug",1);
                        jsonObject.put("type",1);
                    //JsonObject to String
                        get_jsonObject = jsonObject.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    verifyCode = new VerifyCode(get_jsonObject);
                    verifyCode.execute((Void) null);

                }

                break;
        }

    }

    public class VerifyCode extends AsyncTask<Void, Void, Boolean>{

        private final String json;

        VerifyCode(String json){

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
                String getVerifyCode_result = Server.GetVerifyCode(json);
                if(!getVerifyCode_result.trim().equalsIgnoreCase("false")){

                    try{

                        JSONObject jsonObject = new JSONObject(getVerifyCode_result);
                        get_verifyCode = jsonObject.getString("verifyCode");
                        get_resultCode = jsonObject.getInt("errCode");
                        Log.i("verifyCode",""+get_verifyCode);


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

             //Send data to SignUpPhoneKod
                Intent go_signUpPhoneKod = new Intent(SignUpPhone.this,SignUpPhoneKod.class);
                go_signUpPhoneKod.putExtra("kod",get_verifyCode);
                go_signUpPhoneKod.putExtra("mobilNo",numberPhone);
                startActivity(go_signUpPhoneKod);
            }else{
                progressDialog.dismiss();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, "İşlem Başarısız", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(SignUpPhone.this,R.color.colorAccent));
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            }
        }

        @Override
        protected void onCancelled() {

            verifyCode = null;
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
