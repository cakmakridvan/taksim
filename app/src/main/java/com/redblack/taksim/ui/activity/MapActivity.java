package com.redblack.taksim.ui.activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;


import com.redblack.taksim.Main;
import com.redblack.taksim.R;
import com.redblack.taksim.adapters.HistorySearchAdapter;
import com.redblack.taksim.adapters.ListMapAdapter;
import com.redblack.taksim.model.Address;
import com.redblack.taksim.model.HistorySearch;
import com.redblack.taksim.model.ListMapData;

import io.paperdb.Paper;

/**
 * Created by User on 10/2/2017.
 */

public class MapActivity extends AppCompatActivity {

    String TAG = "placeautocomplete";
    TextView txtView;
    private ListMapData[] listMapData;
    private HistorySearch[] historySearches;
    private RecyclerView recyclerView,recyclerView_history;
    private ListMapAdapter listMapAdapter;
    private ImageButton backMap;
    private HistorySearchAdapter historySearchAdapter;

    private String getAddress = "";
    private double get_latitude = 0.0;
    private double get_longitude = 0.0;
    private Address address_home, address_job, address_favorite;
    private String getName_home = "" , getName_job = "", getName_favorite = "";
    private double lat_home = 0.0, lng_home = 0.0, lat_job = 0.0, lng_job = 0.0, lat_favorite = 0.0, lng_favorite = 0.0;

    private String homeDescription, jobDescription, favoriteDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapim);

     //Initialize Paper
        Paper.init(MapActivity.this);



        //History Search List
/*        recyclerView_history = findViewById(R.id.recyclerView_historySearch);
        historySearches = new HistorySearch[]{

                new HistorySearch("Sarıyer,Maslak Mah. Eclipse A-Blok, 234 No"),
                new HistorySearch("Sarıyer,Maslak Mah. Eclipse A-Blok, 234 No"),
                new HistorySearch("Sarıyer,Maslak Mah. Eclipse A-Blok, 234 No"),
                new HistorySearch("Sarıyer,Maslak Mah. Eclipse A-Blok, 234 No"),
                new HistorySearch("Sarıyer,Maslak Mah. Eclipse A-Blok, 234 No"),
                new HistorySearch("Sarıyer,Maslak Mah. Eclipse A-Blok, 234 No")
        };

        historySearchAdapter = new HistorySearchAdapter(historySearches);
        recyclerView_history.setHasFixedSize(true);
        recyclerView_history.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_history.setAdapter(historySearchAdapter);*/


        backMap = findViewById(R.id.back_map);
        backMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });

        // Initialize Places.
        Places.initialize(MapActivity.this, getString(R.string.google_maps_key));
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        //Disable Search icon of autocomplete fragment and change text of it
        autocompleteFragment.setHint("Nereye gitmek istiyorsunuz?");
        ImageView searchIcon = (ImageView)((LinearLayout)autocompleteFragment.getView()).getChildAt(0);
        searchIcon.setVisibility(View.GONE);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                final LatLng location = place.getLatLng();
                get_latitude = location.latitude;
                get_longitude = location.longitude;
                getAddress = place.getName();

/*                txtView.setText(place.getName()+","+location.latitude);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());*/

             //Send Value of selected Place
                Intent intent = new Intent(MapActivity.this,Main.class);
                if(!getAddress.equals("")) {
                    intent.putExtra("place_adres", getAddress);//Address name
                    intent.putExtra("latitude",String.valueOf(get_latitude));//Latitude
                    intent.putExtra("longitude",String.valueOf(get_longitude));//Longitude
                    setResult(RESULT_OK, intent);
                    finish();
                }


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

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

        //txtView = findViewById(R.id.txtView);
        recyclerView = findViewById(R.id.recyclerView_map);

        //ListMapData initialize
        listMapData = new ListMapData[]{
                new ListMapData(homeDescription,R.drawable.homeaddress),
                new ListMapData(jobDescription,R.drawable.workaddress),
                new ListMapData(favoriteDescription,R.drawable.favoriaddress)
        };

        listMapAdapter = new ListMapAdapter(listMapData,MapActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listMapAdapter);

    }
}