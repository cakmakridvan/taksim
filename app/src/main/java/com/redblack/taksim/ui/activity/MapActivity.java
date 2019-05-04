package com.redblack.taksim.ui.activity;


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
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;


import com.redblack.taksim.R;
import com.redblack.taksim.adapters.ListMapAdapter;
import com.redblack.taksim.model.ListMapData;

/**
 * Created by User on 10/2/2017.
 */

public class MapActivity extends AppCompatActivity {

    String TAG = "placeautocomplete";
    TextView txtView;
    private ListMapData[] listMapData;
    private RecyclerView recyclerView;
    private ListMapAdapter listMapAdapter;
    private ImageButton backMap;

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

        backMap = findViewById(R.id.back_map);
        backMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialize Places.
        Places.initialize(MapActivity.this, "AIzaSyBMTYBLLQj1MkSHIPhfAXZZPktdKztLsng");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setHint("Nereye gitmek istiyorsunuz?");
        ImageView searchIcon = (ImageView)((LinearLayout)autocompleteFragment.getView()).getChildAt(0);
        searchIcon.setVisibility(View.GONE);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                txtView.setText(place.getName()+","+place.getId());
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


}