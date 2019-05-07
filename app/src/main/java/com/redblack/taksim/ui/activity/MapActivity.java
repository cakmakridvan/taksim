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
import com.redblack.taksim.model.HistorySearch;
import com.redblack.taksim.model.ListMapData;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapim);

        txtView = findViewById(R.id.txtView);
        recyclerView = findViewById(R.id.recyclerView_map);

     //ListMapData initialize
        listMapData = new ListMapData[]{
                new ListMapData("Ev adresi ekle",R.drawable.homeaddress),
                new ListMapData("İş adresi ekle",R.drawable.workaddress),
                new ListMapData("Favori adres ekle",R.drawable.favoriaddress)
        };

        listMapAdapter = new ListMapAdapter(listMapData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listMapAdapter);

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

                txtView.setText(place.getName()+","+location.latitude);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());

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


}