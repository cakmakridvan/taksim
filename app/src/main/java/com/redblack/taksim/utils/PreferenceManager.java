package com.redblack.taksim.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.redblack.taksim.R;

public class PreferenceManager {

    private Context context;
    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context){

        this.context = context;
        getSharePreference();

    }

    private void getSharePreference(){

        sharedPreferences = context.getSharedPreferences("kayit",Context.MODE_PRIVATE);
    }

    public void writePreference(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("kayit1","INIT OK");
        editor.commit();
    }

    public boolean checkPreference(){

        boolean status = false;
        if(sharedPreferences.getString("kayit","null").equals("null")){

            status = false;
        }else{
            status = true;
        }
        return status;
    }

    public void clearPreference(){

        sharedPreferences.edit().clear().commit();
    }
}
