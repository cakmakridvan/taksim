package com.redblack.taksim;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.redblack.taksim.model.usablecars.GetCars;
import com.redblack.taksim.model.usablecars.ModelCars;
import com.redblack.taksim.ui.activity.CreditCard;
import com.redblack.taksim.ui.activity.MapActivity;
import com.redblack.taksim.ui.activity.StartingLocation;
import com.redblack.taksim.ui.interfaces.TaskLoadedCallback;
import com.redblack.taksim.ui.logintype.MainType;
import com.redblack.taksim.ui.logintype.server.Server;
import com.redblack.taksim.ui.mapdirection.PointsParser;
import com.redblack.taksim.utils.GpsUtils;
import com.redblack.taksim.utils.PreferenceLoginSession;
import com.redblack.taksim.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.paperdb.Paper;

public class Main extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, TaskLoadedCallback {

    private NavigationView navigationView;

    private SupportMapFragment supportMapFragment;
    private double wayLatitude = 0.0, wayLongitude = 0.0;

    private StringBuilder stringBuilder;

    private boolean isContinue = false;
    private boolean isGPS = false;

    private TextView edt_myAddress,edt_destination_address,edt_starting_place;

    private Location location;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    Boolean chaeck = true;
    private Polyline currentPolyline;
    private GoogleMap mMap;

    private double get_latitude = 0.0 , get_selecting_origin_latitude = 0.0;
    private double get_longitude = 0.0 , get_selecting_origin_longitude = 0.0;
    private String get_adres = "" , get_selecting_origin_adres = "";
    private String getSavedAdres = "";

    private FetchURL fetchURL = null;
    private JSONArray jRoutes;
    private JSONArray jLegs;
    private TextView txt_km,txt_duration,txt_price;
    private String getKm = "" , getDuration = "";

    private LinearLayout lytBottom,lytTop;
    private String get_totalPrice = "";

    private Integer get_resultCode = 15; //default value
    private JSONObject jsonObject;
    private String get_jsonObject = "";
    private GetUsableCars getUsableCars = null;
    private String get_token = "";
    private GetCars response_getCars;
    private ArrayList<ModelCars> car_list;
    Timer timer;
    private ImageButton navMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
      //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (savedInstanceState == null) {
            // First incarnation of this activity.

            supportMapFragment.setRetainInstance(true);
            chaeck = false;

        } else {
            // Reincarnated activity. The obtained map is the same map instance in the previous
            // activity life cycle. There is no need to reinitialize it.

            //supportMapFragment.getMapAsync(Main.this);
            chaeck = true;
        }

        //Initializa Paper
          Paper.init(Main.this);
        //get Token from Paper
          get_token = Paper.book().read("token");

        timer=new Timer();

        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        edt_myAddress = findViewById(R.id.edt_myLocation);
        edt_destination_address = findViewById(R.id.edt_destinationAddress);
        edt_starting_place = findViewById(R.id.edt_myLocation);
        //edt_destination_address.setHint("Nereye gitmek istiyorsunuz?");
        //edt_destination_address.setTextColor(R.color.transparent);
        navMenu = findViewById(R.id.btn_navmenu);
        navMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });


        txt_km = findViewById(R.id.total_KM);
        txt_duration = findViewById(R.id.total_duration);
        txt_price = findViewById(R.id.total_price);

      //Selection of Destination address
        edt_destination_address.setOnClickListener((View v) -> {

                Intent a = new Intent(Main.this,MapActivity.class);
                startActivityForResult(a,1);

        });
      //Selection of Starting Place
        edt_starting_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent b = new Intent(Main.this,StartingLocation.class);
                startActivityForResult(b,2);
            }
        });




/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);*/

/*        // Status bar :: Transparent
        Window window = this.getWindow();

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }*/

        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_item_layout);



        /*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Create Json Object
        jsonObject = new JSONObject();

    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }


        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS


                isGPS = isGPSEnable;


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            edt_myAddress.setText("You need to install Google Play Services to use the App properly");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop location updates
        if (googleApiClient != null  &&  googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }



    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    private String getCityName(LatLng myCoordinates) {

        String myCity = "";
        String province = "";
        String address = "";
        String street = "";
        Geocoder geocoder = new Geocoder(Main.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude,1);
            address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getAdminArea();
            street = addresses.get(0).getSubLocality();
            province = addresses.get(0).getFeatureName();
            Log.i("Addresses:","" + address);
            Log.i("City:",""+myCity);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return street + " " + province +  " " + myCity ;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions markerOptions;
        //first clear before maps
        mMap = googleMap;
        mMap.clear();

      if(get_selecting_origin_latitude != 0.0 && get_selecting_origin_longitude != 0.0) {
        //Selecting Starting Place
          LatLng latLng = new LatLng(get_selecting_origin_latitude, get_selecting_origin_longitude);
          markerOptions = new MarkerOptions().position(latLng).title("Konumum");
          markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_human));
          mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
          mMap.addMarker(markerOptions);
          edt_starting_place.setText(get_selecting_origin_adres);
      }else{
        //Current your Location
          LatLng latLng = new LatLng(wayLatitude, wayLongitude);
          markerOptions = new MarkerOptions().position(latLng).title("Konumum");
          markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_human));
          mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
          mMap.addMarker(markerOptions);
      }
      //Destination(Selected Location from Place)
        if(get_latitude != 0.0 && get_longitude != 0.0){
           //Stop taxi's update Location
            if(timer != null){
                timer.cancel();
                timer = null;
            }

            LatLng latLng3 = new LatLng(get_latitude, get_longitude);
            MarkerOptions markerOptions3 = new MarkerOptions().position(latLng3).title(get_adres);
            markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_icon));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng3, 10));
            mMap.addMarker(markerOptions3);
            edt_destination_address.setText(get_adres);

         //Show Invisible Layout at the bottom and top
         lytBottom = findViewById(R.id.lyt_bottom);
         lytBottom.setVisibility(View.VISIBLE);

         lytTop = findViewById(R.id.lyt_top);
         lytTop.setVisibility(View.VISIBLE);

         //Direction route between two Location
            fetchURL = new FetchURL(Main.this);
            fetchURL.execute(getUrl(markerOptions.getPosition(), markerOptions3.getPosition(), "driving"), "driving");
        }
        //Taxi's Location(Default Location)
        else {
            try {
                //Create JsonObject to send WebService
                jsonObject.put("lon",wayLongitude);
                jsonObject.put("lat",wayLatitude);
                jsonObject.put("radius",10000);
                //JsonObject to String
                get_jsonObject = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(get_latitude == 0.0 && get_longitude == 0.0) {
                //Updating Locations of near Taxi at evey 5 second
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        //your method
                        Main.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getUsableCars = new GetUsableCars(get_jsonObject, get_token);
                                getUsableCars.execute((Void) null);
                            }
                        });


                    }
                }, 0, 5000);//put here time 1000 milliseconds=1 second
            }



/*            LatLng latLng2 = new LatLng(41.113130, 29.020156);
            MarkerOptions markerOptions2 = new MarkerOptions().position(latLng2).title("taxi");
            markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 9));
            mMap.addMarker(markerOptions2);*/


        }
    }

    @Override
    public void onBackPressed() {
      //Delete Selected Location
        //Paper.book().delete("SelectedLocation");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profil) {
            // Handle the camera action
        } else if (id == R.id.nav_creditkart) {

            startActivity(new Intent(Main.this,CreditCard.class));



/*            MainFragment mainFragment = new MainFragment();
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, mainFragment);
            fragmentTransaction.commit();*/

        } else if (id == R.id.nav_adreslerim) {

        } else if (id == R.id.nav_bildirimler) {

        }else if(id == R.id.exit){

            new SweetAlertDialog(Main.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.uygulama_cikis))
                    .setContentText(getString(R.string.hesap_degistir))
                    .setCancelText(getString(R.string.iptal))
                    .setConfirmText(getString(R.string.hesap_cikis))
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            //Close Session, Logout process
                            new PreferenceLoginSession(Main.this).clearPreference();
                            startActivity(new Intent(Main.this,MainType.class));
                            finish();

                            //When user Logout,delete token
                            Paper.book().delete("token");

                        }
                    })
                    .show();



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            //locationTv.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());

            wayLatitude = location.getLatitude();
            wayLongitude = location.getLongitude();
            //txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));

            //Geocoding
            LatLng lat_lng = new LatLng(wayLatitude,wayLongitude);
            String cityName = getCityName(lat_lng);
            edt_myAddress.setText(cityName);

    /*
          //Get Saved Selected Location Name
            getSavedAdres = Paper.book().read("SelectedLocation");
            if(!TextUtils.isEmpty(getSavedAdres)){

                edt_destination_address.setText(getSavedAdres);
            }
    */

            if(chaeck == false) {

                supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                supportMapFragment.getMapAsync(Main.this);
            }


        }

        startLocationUpdates();

    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            //locationTv.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());

            Log.i("Tag","Latitude : " + location.getLatitude() + "Longitude : " + location.getLongitude());

            wayLatitude = location.getLatitude();
            wayLongitude = location.getLongitude();


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(Main.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1){
          //Getting Location info of Destination address from MapActivity
            if(resultCode == RESULT_OK){
             //getting value of Selected Place information from MapActivity
                get_adres = data.getStringExtra("place_adres");
                get_latitude = Double.parseDouble(data.getStringExtra("latitude"));
                get_longitude = Double.parseDouble(data.getStringExtra("longitude"));
            }
        }
      //Getting Location info of StartingPlace from StartingLocation
        else if(requestCode == 2){
            //Getting Location info of StartingPlace address from StartingLocation
            if(resultCode == RESULT_OK){
                get_selecting_origin_adres = data.getStringExtra("origin_place_adres");
                get_selecting_origin_latitude = Double.parseDouble(data.getStringExtra("origin_latitude"));
                get_selecting_origin_longitude = Double.parseDouble(data.getStringExtra("origin_longitude"));
            }



        }
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_server_key);
        return url;
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }



    public class FetchURL extends AsyncTask<String, Void, String> {
        Context mContext;
        String directionMode = "driving";

        public FetchURL(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {
            // For storing data from web service

            String data = "";
            directionMode = strings[1];
            try {
                // Fetching the data from web service
                data = downloadUrl(strings[0]);
                JSONObject jObject = new JSONObject(data);
                jRoutes = jObject.getJSONArray("routes");
                /** Traversing all routes */
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<>();
                    //get km and duration
                    JSONObject distance = jLegs.getJSONObject(0).getJSONObject("distance");
                    getKm = distance.getString("text");
                    Log.i("Distance",getKm);
                    JSONObject duration = jLegs.getJSONObject(0).getJSONObject("duration");
                    getDuration = duration.getString("text");
                    getDuration = getDuration.replace("mins","dk"); //replace mins to dk
                    Log.i("Duration",getDuration);
                }

                get_totalPrice = Utility.taksimetre(getKm);

                    Log.d("mylog", "Background task data " + data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            PointsParser parserTask = new PointsParser(mContext, directionMode);
            // Invokes the thread for parsing the JSON data
            parserTask.execute(s);

            txt_km.setText(getKm);
            txt_duration.setText(getDuration);
            txt_price.setText(get_totalPrice);

        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);
                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();
                // Connecting to url
                urlConnection.connect();
                // Reading data from url
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                Log.d("mylog", "Downloaded URL: " + data.toString());
                br.close();
            } catch (Exception e) {
                Log.d("mylog", "Exception downloading URL: " + e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }
    }

    public class GetUsableCars extends AsyncTask<Void, Void, Boolean> {

        private final String json;
        private final String token;

        GetUsableCars(String json, String token){

            this.json = json;
            this.token = token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //progressDialog.setContentView(R.layout.custom_progress);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                String getUsableCars_result = Server.GetUsableCars(json,token);
                if(!getUsableCars_result.trim().equalsIgnoreCase("false")){
                    try{
                        response_getCars = new Gson().fromJson(getUsableCars_result,GetCars.class);
                        car_list = response_getCars.getData_list();
                        Log.i("UsableCarList",""+car_list);

                        JSONObject jsonObject = new JSONObject(getUsableCars_result);
                        get_resultCode = jsonObject.getInt("errCode");
                        Log.i("resultCode",""+get_resultCode);
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

                if(car_list.size() > 0){
                    //First Clear Map
                    mMap.clear();

                    if(get_selecting_origin_latitude == 0.0 && get_selecting_origin_longitude == 0.0) {
                      //Current your Location
                        LatLng latLng = new LatLng(wayLatitude, wayLongitude);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Konumum");
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_human));
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        mMap.addMarker(markerOptions);
                    }else{
                      //Selecting Starting Place
                        LatLng latLng = new LatLng(get_selecting_origin_latitude, get_selecting_origin_longitude);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Konumum");
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_human));
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        mMap.addMarker(markerOptions);
                    }
                    for(int i = 0; i<car_list.size(); i++){

                        LatLng latLng2 = new LatLng(car_list.get(i).getLat(),car_list.get(i).getLon());
                        MarkerOptions markerOptions2 = new MarkerOptions().position(latLng2).title(car_list.get(i).getCarNo());
                        markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon));
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 9));
                        mMap.addMarker(markerOptions2);
                    }
                }
            }

            else{

/*                Snackbar snackbar = Snackbar.make(coordinatorLayout, "İşlem Başarısız", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(SignUp.this,R.color.colorAccent));
                snackbar.show();*/

            }
        }

        @Override
        protected void onCancelled() {

            getUsableCars = null;

        }
    }

}
