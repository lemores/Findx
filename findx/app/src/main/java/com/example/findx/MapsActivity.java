package com.example.findx;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.location.Address;
import android.location.Geocoder;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.String.valueOf;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.StringValue;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapsActivity";

    NetworkInfo wifiCheck;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 16.7f;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private CountDownTimer waitTimer;
    private List<LocalItem> localList;
    private SlidingUpPanelLayout mLayout;
    private FirebaseAuth auth;
    int countAssist = 0;
    String CurrentuserID;
    DatabaseReference reff;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        auth = FirebaseAuth.getInstance();
        CurrentuserID = auth.getCurrentUser().getUid();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fillLocalList();//preeenche a searchbar baseado no array
        fabOnclick();//abre a navbar
        centerFab();//centraliza no usuario

        stopLoading();
        erase();
        final BottomNavigationView mNavbar = (BottomNavigationView) findViewById(R.id.Navbar);
        final Animation mShowNavbar = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.show_navbar);
        mNavbar.setVisibility(VISIBLE);
        mNavbar.startAnimation(mShowNavbar);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.activity_main);

        AutoCompleteTextView editText = findViewById(R.id.searchbar);
        AutoCompleteLocalAdapter adapter = new AutoCompleteLocalAdapter(this, localList);
        editText.setAdapter(adapter);

        wifiCheck();
        getLocationPermission();
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.Navbar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {


        HashMap<String , String > map = new HashMap<>();

        map.put("a", "duaaf");
        map.put("b", "cvzf");
        map.put("c", "dudzfbxaaf");


        for(String v : map.values()){
            Log.d("MapsActivity", v);
        }
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        Toast.makeText(this, "Bem Vindo!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: mapa ta pronto");
        getWindow().setStatusBarColor(Color.parseColor("#20111111"));
        getWindow().setNavigationBarColor(Color.parseColor("#20111111"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        try {
            boolean sucess = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle));
            if (!sucess) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Cant't find style. ERROR: ", e);
        }


        // TODO Criar um ID para cada markerOption, e então comparar caso assistencia for clicada (bool),
        //  checar seu id, e mudar child do Suggestion para seu id

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);

        // Conta childs de número de assistências
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias");
        reff.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot)
            {
                countAssist = (int) dataSnapshot.getChildrenCount();
                Toast.makeText(MapsActivity.this, countAssist +" Assistências", Toast.LENGTH_SHORT).show();


                for (int i = 1; i <= countAssist; i++)
                {
                    reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child(Integer.toString(i));
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            String nome = dataSnapshot.child("nome").getValue().toString();
                            Float latitude = Float.parseFloat(dataSnapshot.child("latitude").getValue().toString());
                            Float longitude = Float.parseFloat(dataSnapshot.child("longitude").getValue().toString());


                                // adiciona um marcador no mapa
                                LatLng posicao = new LatLng(latitude, longitude);
                                MarkerOptions assistencia = new MarkerOptions();
                                assistencia.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                mMap.addMarker(assistencia.position(posicao).title(nome));//.snippet("Population: 4,137,400"));
                                final Marker markAssist = mMap.addMarker(assistencia);
                                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        Toast.makeText(MapsActivity.this, " Assistência: " + markAssist.getTag(), Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });

                            }



                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }

                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }

        });


    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent suggestion = new Intent(getApplication(), Suggestion.class);
        startActivity(suggestion);
        Toast.makeText(MapsActivity.this, "skhfsu " +(marker.getTag()), Toast.LENGTH_SHORT).show();
        return false;
    }


    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
    public void stopStop(){
        final ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);
        CameraPosition cameraPosition = mMap.getCameraPosition();
        if(cameraPosition.zoom == DEFAULT_ZOOM) {
            waitTimer.cancel();
            spinner.setVisibility(GONE);
        } else{
        }
    }
    public void stopLoading(){
        waitTimer = new CountDownTimer(50000, 1000) {

            public void onTick(long millisUntilFinished) {
                getDeviceLocation();
                stopStop();
            }

            public void onFinish() {
                getDeviceLocation();
                buildAlertMessageFail();
            }
        }.start();
    }
    private void buildAlertMessageFail() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ae mano n te achei, sai da caverna carai! taokey?")
                .setCancelable(false)
                .setPositiveButton("tentar dnv", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        stopLoading();
                    }
                })
                .setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                        System.exit(0);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public void erase() {
        final AutoCompleteTextView editText = findViewById(R.id.searchbar);
        final ImageView clear = findViewById(R.id.clear);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length() > 0){
                    clear.setVisibility(View.VISIBLE);
                }else{
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clear.getVisibility() == VISIBLE) {
                    editText.setText("");
                }
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final TextView mTextMessage2 = (TextView) findViewById(R.id.message2);
            final TextView mTextMessage3 = (TextView) findViewById(R.id.message3);
            final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            final LinearLayoutCompat mFrag2 = (LinearLayoutCompat) findViewById(R.id.frag2);
            final LinearLayoutCompat mFrag3 = (LinearLayoutCompat) findViewById(R.id.frag3);
            final BottomNavigationView mNavbar = (BottomNavigationView) findViewById(R.id.Navbar);
            final Animation mShowFrag = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.show_frag);
            final Animation mHideFrag = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.hide_frag);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(mFrag2.getVisibility() == VISIBLE || mFrag3.getVisibility() == VISIBLE) {
                        scrollView.setVisibility(GONE);
                        mFrag2.startAnimation(mHideFrag);
                        mFrag3.startAnimation(mHideFrag);
                        mFrag2.setVisibility(GONE);
                        mFrag3 .setVisibility(GONE);
                    }
                    else{
                        scrollView.setVisibility(GONE);
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if(mFrag2.getVisibility() == VISIBLE && mNavbar.getVisibility() == VISIBLE) {
                        scrollView.setVisibility(GONE);
                        mFrag2.setVisibility(VISIBLE);
                        mFrag3.setVisibility(GONE);
                        mTextMessage2.setText(R.string.title_dashboard);
                    }
                    if(mFrag3.getVisibility() == VISIBLE && mNavbar.getVisibility() == VISIBLE) {
                        scrollView.setVisibility(GONE);
                        mFrag2.startAnimation(mShowFrag);
                        mFrag2.setVisibility(VISIBLE);
                        mFrag3.startAnimation(mHideFrag);
                        mFrag3.setVisibility(GONE);
                        mTextMessage2.setText(R.string.title_dashboard);
                    }
                    if(mFrag3.getVisibility() == GONE && mFrag2.getVisibility() == GONE && mNavbar.getVisibility() == VISIBLE){
                        scrollView.setVisibility(GONE);
                        mFrag2.startAnimation(mShowFrag);
                        mFrag2.setVisibility(VISIBLE);
                    }
                    return true;
                case R.id.navigation_notifications:
                    if(mFrag3.getVisibility() == VISIBLE && mNavbar.getVisibility() == VISIBLE) {
                        scrollView.setVisibility(VISIBLE);
                        mFrag3.setVisibility(VISIBLE);
                        mFrag2.setVisibility(GONE);
                        mTextMessage3.setText(R.string.title_notifications);
                    }
                    if(mFrag2.getVisibility() == VISIBLE && mNavbar.getVisibility() == VISIBLE) {
                        scrollView.setVisibility(VISIBLE);
                        mFrag3.startAnimation(mShowFrag);
                        mFrag3.setVisibility(VISIBLE);
                        mFrag2.setVisibility(GONE);
                        mFrag2.startAnimation(mHideFrag);
                        mTextMessage3.setText(R.string.title_notifications);
                    }
                    if(mFrag3.getVisibility() == GONE && mFrag2.getVisibility() == GONE  && mNavbar.getVisibility() == VISIBLE){
                        mFrag3.setVisibility(VISIBLE);
                        scrollView.setVisibility(VISIBLE);
                        mFrag3.startAnimation(mShowFrag);
                    }
                    return true;
            }
            return false;
        }
    };
    private void centerFab() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final FloatingActionButton mCenter = (FloatingActionButton) findViewById(R.id.center_button);

        mCenter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        backDeviceLocation();
                    }
                    else {
                        Toast.makeText(MapsActivity.this, "Nao te encontrei", Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception ex) {
                    Log.e(TAG, "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
                    ex.printStackTrace();
                }
            }
        });
    }
    private void fabOnclick() {
        final FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton navfab = (FloatingActionButton) findViewById(R.id.navfab);
        final BottomNavigationView mNavbar = (BottomNavigationView) findViewById(R.id.Navbar);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        final LinearLayoutCompat mFrag2 = (LinearLayoutCompat) findViewById(R.id.frag2);
        final LinearLayoutCompat mFrag3 = (LinearLayoutCompat) findViewById(R.id.frag3);
        final Animation mHideFrag = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.hide_frag);
        final Animation mShowButton = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.show_button);
        final Animation mHideButton = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.hide_button);
        final Animation mShowNavbar = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.show_navbar);
        final Animation mHideNavbar = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.hide_navbar);
        final Animation showNavfab = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.show_navfab);
        final Animation hideNavfab = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.hide_navfab);
        final Animation mShowLay = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.show_layout);
        final Animation mHideLay = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.hide_layout);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFrag2.getVisibility() == VISIBLE || mFrag3.getVisibility() == VISIBLE || mFrag2.getVisibility() == VISIBLE && mFrag3.getVisibility() == VISIBLE) {
                    mFrag3.startAnimation(mHideFrag);
                    mFrag3.setVisibility(GONE);
                    mFrag2.startAnimation(mHideFrag);
                    mFrag2.setVisibility(GONE);
                    scrollView.setVisibility(GONE);
                }
                if (mNavbar.getVisibility() == VISIBLE) {
                    navfab.startAnimation(hideNavfab);
                    mNavbar.setVisibility(View.GONE);
                    mNavbar.startAnimation(mHideNavbar);
                    mFab.startAnimation(mHideButton);
                }
                else {
                    navfab.startAnimation(showNavfab);
                    mNavbar.setVisibility(VISIBLE);
                    mNavbar.startAnimation(mShowNavbar);
                    mFab.startAnimation(mShowButton);
                }
            }
        });
    }
    private void fillLocalList() {

        localList = new ArrayList<>();
        localList.add(new LocalItem("Rua siqueira bueno", R.drawable.ic_location_on_black_16dp, "celular"));
        localList.add(new LocalItem("Rua dos trilhos", R.drawable.ic_location_on_black_16dp, "geladeira"));
        localList.add(new LocalItem("Rua marcial", R.drawable.ic_location_on_black_16dp,"notebook"));
        localList.add(new LocalItem("Rua cassandoca", R.drawable.ic_location_on_black_16dp, "cu"));
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);
        final FloatingActionButton mCenter = (FloatingActionButton) findViewById(R.id.center_button);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Location currentLocation = (Location) task.getResult();
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: te achei!");
                            try {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            }catch (Exception e){
                                Log.e(TAG, "getDeviceLocation: SecurityException: ");
                            }
                        }
                        else{
                            if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            }
                            Log.d(TAG, "onComplete: n te achei");
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: ");
            if( manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            }
        }
    }
    public void backDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);
        final FloatingActionButton mCenter = (FloatingActionButton) findViewById(R.id.center_button);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Location currentLocation = (Location) task.getResult();
                        if(task.isSuccessful()){

                            Log.d(TAG, "onComplete: te achei!");
                            try {
                                moveCamera2(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            }catch (Exception e){
                                Log.e(TAG, "getDeviceLocation: SecurityException: ");
                            }
                        }
                        else{
                            if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            }
                            Log.d(TAG, "onComplete: n te achei");
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: ");
            if( manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            }
        }
    }
    public void moveCamera(LatLng latLng, float zoom){
        final ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
    public void moveCamera2(LatLng latLng, float zoom){
        final ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), 1500, null);
    }

    private void initMap() {
        try {
            Log.d(TAG, "initMap: initializing map");
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            mapFragment.getMapAsync(MapsActivity.this);
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Cant't find style. ERROR: ", e);
        }
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        final ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
    public void wifiCheck() {
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiCheck.isConnected()) {
            statusCheck();
        } else {
            buildAlertMessageNoWifi();
            statusCheck();
        }
    }
    private void buildAlertMessageNoWifi() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ae mano o Wifi ta desligado,tem que liga samerda pro app funfar! taokey?")
                .setCancelable(false)
                .setPositiveButton("ta bom", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Continuar sem", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ae mano o GPS ta desligado,tem que liga samerda pro app funfar! taokey?")
                .setCancelable(false)
                .setPositiveButton("ta bom", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                        System.exit(0);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void onMapSearch(View view) {
        AutoCompleteTextView locationSearch = (AutoCompleteTextView) findViewById(R.id.searchbar);
        String location = locationSearch.getText().toString();
        List<Address>addressList = null;
        closeKeyboard();
        if (!location.isEmpty()) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch(Exception ex) {
                Log.e(TAG, "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
                ex.printStackTrace();
            }
            if (addressList != null && addressList.size() != 0) {

                Address address = addressList.get(0);
                final LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                //MarkerOptions options = new MarkerOptions();
                //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                //mMap.addMarker(options.position(latLng).title("tem que po um naome nessa merdqa"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM),1500, null);
            }
            else
            {
                Toast.makeText(MapsActivity.this, "N achei", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(MapsActivity.this, "Num tem nada :(", Toast.LENGTH_SHORT).show();

        }
    }
}
