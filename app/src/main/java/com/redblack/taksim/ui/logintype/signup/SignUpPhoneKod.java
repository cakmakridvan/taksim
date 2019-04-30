package com.redblack.taksim.ui.logintype.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.redblack.taksim.R;
import com.redblack.taksim.ui.logintype.server.Server;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpPhoneKod extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back,next;
    private ProgressDialog progressDialog;
    private String get_mesaj_result = "";
    private Bundle extras;
    private String get_kod = "";
    private String get_number = "";
    private EditText entered_kod;
    private String get_enteredKod = "";
    private JSONObject jsonObject;
    private String get_jsonObject = "";
    private CustomerLoginm customerLoginm = null;
    private String get_token = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_phone_kod);

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
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_bck_phone_kod:
                finish();
                break;

            case R.id.btn_next_phone_kod:

                get_enteredKod = entered_kod.getText().toString();

                if(!TextUtils.isEmpty(get_enteredKod)){

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
            //progressDialog.setContentView(R.layout.custom_progress);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                String getVerifyMobile_result = Server.CustomerLogin(json);
                if(!getVerifyMobile_result.trim().equalsIgnoreCase("false")){

                    try{

                        JSONObject jsonObject = new JSONObject(getVerifyMobile_result);
                        //String get_verifyMobile = jsonObject.getString("verifyCode");
                        get_mesaj_result = "true";
                        Log.i("jsonObject",""+jsonObject);

                        //get Token
                        get_token = Server.token;
                        Log.i("token:","" + get_token);


                    }catch (JSONException e){
                        Log.i("Exception",e.getMessage());
                    }
                }else{
                    get_mesaj_result = "false";
                }

            }catch (Exception e){
                Log.i("Exception",e.getMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


            if(get_mesaj_result.equals("true")){
                progressDialog.dismiss();

                Intent go_signup = new Intent(SignUpPhoneKod.this,SignUp.class);
                go_signup.putExtra("mobileNO",get_number);
                go_signup.putExtra("token",get_token);
                startActivity(go_signup);
            }

            else{
                progressDialog.dismiss();
            }
        }

        @Override
        protected void onCancelled() {

            customerLoginm = null;
            progressDialog.dismiss();
        }
    }
}
