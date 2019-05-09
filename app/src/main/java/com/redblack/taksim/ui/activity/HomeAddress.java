package com.redblack.taksim.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.redblack.taksim.R;
import com.redblack.taksim.model.Address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;

public class HomeAddress extends AppCompatActivity {

    private ImageButton bck_home;
    //private TextView text;
    private double get_latitude = 0.0, get_longitude = 0.0;
    private String get_name = "";
    private Address address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_address);

     //Initialize Paper
        Paper.init(HomeAddress.this);

        bck_home = findViewById(R.id.back_home);
        //text = findViewById(R.id.txtView_home);

        bck_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialize Places.
        Places.initialize(HomeAddress.this, getString(R.string.google_maps_key));
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_home);

        //Disable Search icon of autocomplete fragment and change text of it
        autocompleteFragment.setHint("Ev adresinizi giriniz?");
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
                get_name = place.getName();

                address = new Address(get_name,get_latitude,get_longitude);
                //Saved Selected Home Addess
                Paper.book().write("home_address",address);

/*                text.setText(place.getName()+","+place.getId());
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getLatLng());*/

                finish();


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });
    }
}
