package com.redblack.taksim.ui.logintype.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.redblack.taksim.R;
import com.redblack.taksim.ui.logintype.server.Server;
import com.redblack.taksim.ui.logintype.signup.SignUp;
import com.redblack.taksim.ui.logintype.signup.SignUpPhone;
import com.redblack.taksim.ui.logintype.signup.SignUpPhoneKod;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPhone extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back,next;
    private ProgressDialog progressDialog;
    private String get_verifyCode = "";
    private Integer get_resultCode = 15; //Error reultCode is equal 15
    private Bundle extras;
    private String get_mobilNo = "";
    private VerifyCode verifyCode = null;
    private JSONObject jsonObject;
    private String get_jsonObject = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_phone);

        //get code and mhoneNumber form SignUpPhone
        extras = getIntent().getExtras();
        if(extras != null){

            get_mobilNo = extras.getString("mobileNO");
        }

        jsonObject = new JSONObject();

        //Progress Diaolog initialize
        progressDialog = new ProgressDialog(LoginPhone.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setIndeterminate(true);

        back = findViewById(R.id.btn_bck_login_phone);
        next = findViewById(R.id.btn_next_login_phone);

        back.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_bck_login_phone:
                finish();
                break;

            case R.id.btn_next_login_phone:
                //startActivity(new Intent(this,LoginKod.class));

                try {
                    //Create JsonObject to send WebService
                    jsonObject.put("mobile",get_mobilNo);
                    jsonObject.put("debug",1);
                    jsonObject.put("type",1);
                    //JsonObject to String
                    get_jsonObject = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                verifyCode = new VerifyCode(get_jsonObject);
                verifyCode.execute((Void) null);

                break;
        }
    }

    public class VerifyCode extends AsyncTask<Void, Void, Boolean> {

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
            //progressDialog.setContentView(R.layout.custom_progress);
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
                Intent go_signUpPhoneKod = new Intent(LoginPhone.this,LoginKod.class);
                go_signUpPhoneKod.putExtra("kod",get_verifyCode);
                go_signUpPhoneKod.putExtra("mobilNo",get_mobilNo);
                startActivity(go_signUpPhoneKod);
            }

            else{
                Toast.makeText(LoginPhone.this,"İşlem Başarısız",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }

        @Override
        protected void onCancelled() {

            verifyCode = null;
            progressDialog.dismiss();
        }
    }
}
