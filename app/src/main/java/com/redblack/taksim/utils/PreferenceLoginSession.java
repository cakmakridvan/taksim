package com.redblack.taksim.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceLoginSession {

    private Context context;
    private SharedPreferences sharedPreferences;

    public PreferenceLoginSession(Context context){

        this.context = context;
        getSharePreference();

    }

    private void getSharePreference(){

        sharedPreferences = context.getSharedPreferences("login",Context.MODE_PRIVATE);
    }

    public void writePreference(String token){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("login_session",token);
        editor.commit();
    }

    public boolean checkPreference(){

        boolean status = false;
        if(sharedPreferences.getString("login_session","null").equals("null")){

            status = false;
        }else{
            status = true;
        }
        return status;
    }

    public void clearPreference(){

        sharedPreferences.edit().clear().commit();
    }

    public String getToken(){

        return sharedPreferences.getString("login_session","");
    }
}
