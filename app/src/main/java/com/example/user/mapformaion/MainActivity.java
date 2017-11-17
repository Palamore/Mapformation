package com.example.user.mapformaion;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    protected static final String TAG = "MainActivity";
    boolean logo_on = true;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int RC_LOCATION = 1;
    private static final int RC_LOCATION_UPDATE = 2;
    protected LocationCallback mLocationCallback;
    EditText search;
    protected Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(logo_on){
            logo_on = false;
            Intent start_intent = new Intent(this, imageAct.class);
            startActivity(start_intent);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        search = (EditText)this.findViewById(R.id.search);
       final String searchS = search.getText().toString();

        FusedLocationProviderClient mFusedLocationClient
                = LocationServices.getFusedLocationProviderClient(this);

        Button btn = (Button)findViewById(R.id.bt2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mMap.moveCamera(CameraUpdateFactory.zoomIn());
                toAddress(searchS);

            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



    public void toAddress(String searched) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(searched,1);
            if (addresses.size() >0) {
                Address address = addresses.get(0);
                Log.w(TAG,searched + address.getLatitude() + address.getLongitude());
            onMapMoveTo(searched,address.getLatitude(),address.getLongitude() );

            }
        } catch (IOException e) {
            Log.e(TAG, "Failed in using Geocoder",e);
        }
    }

    public void onMapMoveTo(String keyword,double lat, double lng) {

        LatLng searched = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(searched).title("Marker in " + keyword ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(searched));

    }
}
