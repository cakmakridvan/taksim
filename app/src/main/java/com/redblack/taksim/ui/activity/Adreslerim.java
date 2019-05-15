package com.redblack.taksim.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;

import com.redblack.taksim.R;
import com.redblack.taksim.adapters.AdreslerimDataAdapter;
import com.redblack.taksim.model.Address;
import com.redblack.taksim.model.ListMapData;

import io.paperdb.Paper;

public class Adreslerim extends AppCompatActivity {

    private ImageButton btn_bck;
    private String homeDescription, jobDescription, favoriteDescription;
    private AdreslerimDataAdapter adreslerimDataAdapter;
    private ListMapData[] listMapData;
    private RecyclerView recyclerView;
    private Address address_home, address_job, address_favorite;
    private String getName_home = "" , getName_job = "", getName_favorite = "";
    private double lat_home = 0.0, lng_home = 0.0, lat_job = 0.0, lng_job = 0.0, lat_favorite = 0.0, lng_favorite = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adreslerim);

        recyclerView = findViewById(R.id.recyclerView_adreslerim);

        Paper.init(Adreslerim.this);

        btn_bck = findViewById(R.id.bck_btn_adreslerim);
        btn_bck.setOnClickListener((View) ->{

            finish();
        });


        //Get Saved Home Address info
        address_home = Paper.book().read("home_address");
        if(address_home != null) {
            getName_home = address_home.getName();
            lat_home = address_home.getLat();
            lng_home = address_home.getLng();
            Log.i("name", getName_home);

            homeDescription = "Ev : " + getName_home;
        }else{
            homeDescription = "Ev adresi ekle";
        }

        //Get Saved Job Address info
        address_job = Paper.book().read("job_address");
        if(address_job != null) {
            getName_job = address_job.getName();
            lat_job = address_job.getLat();
            lng_job = address_job.getLng();
            Log.i("name", getName_job);

            jobDescription = "İş : " + getName_job;
        }else{
            jobDescription = "İş adresi ekle";
        }

        //Get Saved Favorite Address info
        address_favorite = Paper.book().read("favorite_address");
        if(address_favorite != null) {
            getName_favorite = address_favorite.getName();
            lat_favorite = address_favorite.getLat();
            lng_favorite = address_favorite.getLng();
            Log.i("name", getName_favorite);

            favoriteDescription = "Favori : " + getName_favorite;
        }else{
            favoriteDescription = "Favori adres ekle";
        }


        //ListMapData initialize
        listMapData = new ListMapData[]{
                new ListMapData(homeDescription,R.drawable.homeaddress),
                new ListMapData(jobDescription,R.drawable.workaddress),
                new ListMapData(favoriteDescription,R.drawable.favoriaddress)
        };

        adreslerimDataAdapter = new AdreslerimDataAdapter(listMapData,Adreslerim.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adreslerimDataAdapter);
    }
}
