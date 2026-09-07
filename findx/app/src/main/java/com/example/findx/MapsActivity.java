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
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseUser auth;
    int countAssist = 0;
    String CurrentuserID;
    DatabaseReference reff;

    //TODO Mudar icone do favorito quando apertado
    //TODO Configurar página de favoritos, e remove-los
    //TODO LogOut User

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        //auth = FirebaseAuth.getInstance().getCurrentUser();
        //CurrentuserID = auth.getUid();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fillLocalList();//preeenche a searchbar baseado no array
        fabOnclick();//abre a navbar
        centerFab();//centraliza no usuario
        userPage();//pag do usuario
        FloatingActionButton btn_user = (FloatingActionButton) findViewById(R.id.btn_user);

        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentActivity(v);
            }
        });

        stopLoading();
        erase();
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.activity_main);

        AutoCompleteTextView editText = findViewById(R.id.searchbar);
        AutoCompleteLocalAdapter adapter = new AutoCompleteLocalAdapter(this, localList);
        editText.setAdapter(adapter);

        final FloatingActionButton close = (FloatingActionButton) findViewById(R.id.close_button);
        final Animation hideClose = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.hide_close);
        close.hide();

        wifiCheck();
        getLocationPermission();
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
        //Toast.makeText(this, "Bem Vindo!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: mapa ta pronto");
        getWindow().setStatusBarColor(Color.parseColor("#20111111"));
        getWindow().setNavigationBarColor(Color.parseColor("#20111111"));
        FloatingActionButton btn_user = (FloatingActionButton) findViewById(R.id.btn_user);

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
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle));
            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Cant't find style. ERROR: ", e);
        }


        // TODO

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);

        // Conta childs de número de assistências
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias");
        reff.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot)
            {
                countAssist = (int) dataSnapshot.getChildrenCount();
                //Toast.makeText(MapsActivity.this, countAssist +" Assistências", Toast.LENGTH_SHORT).show();


                //Poem os marcadores de acordo com a quantidade registrada (countAssist)
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


                            //adiciona um marcador no mapa
                            LatLng posicao = new LatLng(latitude, longitude);
                            MarkerOptions assistencia = new MarkerOptions();
                            assistencia.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                            mMap.addMarker(assistencia.position(posicao).title(nome));//.snippet("Population: 4,137,400"));


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
        //Toast.makeText(MapsActivity.this,""+marker.getId(),Toast.LENGTH_SHORT).show();
        String Id = String.valueOf(marker.getId());
        suggestion.putExtra("Marker Id", Id);
        startActivity(suggestion);

        return false;
    }


    @Override
    public void onBackPressed() {
        RelativeLayout bottom_sheet = (RelativeLayout)findViewById(R.id.bottom_sheet);
        ImageView arrow = (ImageView)findViewById(R.id.arrow);
        if(mLayout.getPanelState() == PanelState.ANCHORED){
            mLayout.setPanelHeight(100);
        }
        if(mLayout.getPanelState() == PanelState.EXPANDED){
            mLayout.setPanelHeight(500);
        }
        if(mLayout.getPanelState() == PanelState.COLLAPSED){
            mLayout.setPanelHeight(100);
        }
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
            mLayout.setPanelHeight(100);

        } else {
            super.onBackPressed();
            mLayout.setPanelHeight(100);
        }
    }
    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, userPage.class);
        intent.putExtra(userPage.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(userPage.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
    public void stopStop(){
        final ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);
        CameraPosition cameraPosition = mMap.getCameraPosition();
        if(cameraPosition.zoom == DEFAULT_ZOOM) {
            waitTimer.cancel();
            mLayout.setPanelHeight(100);
            mLayout.setPanelState(PanelState.ANCHORED);
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
    public void userPage() {
        final View map = (View) findViewById(R.id.map);
        final FloatingActionButton btn_user = (FloatingActionButton) findViewById(R.id.btn_user);
        final FloatingActionButton close = (FloatingActionButton) findViewById(R.id.close_button);
        final Animation showClose = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.show_center);
        final Animation hideClose = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.hide_center);
        final Animation showClosebtn = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.show_close);
        final Animation hideClosebtn = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.hide_close);
        final AppCompatImageView pag_user = (AppCompatImageView) findViewById(R.id.pag_user);
        final Animation showUser = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.show_user_page);
        final Animation hideUser = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.hide_user_page);
        final Animation user2_close = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.user2_close);
        final Animation user2_open = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.user2_open);
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setVisibility(GONE);
                btn_user.hide();
                pag_user.setVisibility(VISIBLE);
                pag_user.startAnimation(showUser);
                close.show();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_user.show();
                pag_user.setVisibility(GONE);
                pag_user.startAnimation(hideUser);
                pag_user.setVisibility(GONE);
                close.hide();
                map.setVisibility(VISIBLE);
            }
        });
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
                        Toast.makeText(MapsActivity.this, "Não foi possível te localizar", Toast.LENGTH_SHORT).show();
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
                else {
                    navfab.startAnimation(showNavfab);
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
        builder.setMessage("Ligar o wifi para melhorar buscas?")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
        builder.setMessage("Ligue o gps para o app funcionar")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

                MarkerOptions options = new MarkerOptions();
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                mMap.addMarker(options.position(latLng).title("tem que po um naome nessa merdqa"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM),1500, null);
            }
            else
            {
                Toast.makeText(MapsActivity.this, "Não localizado", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(MapsActivity.this, "Não localizado", Toast.LENGTH_SHORT).show();

        }
    }
}
