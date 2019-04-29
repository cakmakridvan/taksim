package com.redblack.taksim.ui.logintype.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.redblack.taksim.R;
import com.redblack.taksim.ui.logintype.server.Server;
import com.redblack.taksim.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpPhone extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btn_back,btn_next;
    private EditText number;
    private String getNumber = "";
    private ProgressDialog progressDialog;
    private String get_mesaj_result = "";
    private VerifyCode verifyCode = null;
    private JSONObject jsonObject;
    private String get_jsonObject = "";
    private String numberPhone = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_phone);

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

                    Toast.makeText(this,"Numaranızı Kontrol Edin",Toast.LENGTH_LONG).show();
                }

                else{

                    numberPhone = "+90" + getNumber;

                    try {
                        //Create JsonObject
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
            //progressDialog.setContentView(R.layout.custom_progress);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                String getVerifyCode_result = Server.GetVerifyCode(json);
                if(!getVerifyCode_result.trim().equalsIgnoreCase("false")){

                    try{

                        JSONObject jsonObject = new JSONObject(getVerifyCode_result);
                        String get_verifyCode = jsonObject.getString("verifyCode");
                        get_mesaj_result = "true";
                        Log.i("verifyCode",""+get_verifyCode);


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

                startActivity(new Intent(SignUpPhone.this,SignUpPhoneKod.class));
            }


            else{
                progressDialog.dismiss();
            }
        }

        @Override
        protected void onCancelled() {

            progressDialog.dismiss();
        }
    }
}
