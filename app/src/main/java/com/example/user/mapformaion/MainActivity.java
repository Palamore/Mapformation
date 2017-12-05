package com.example.user.mapformaion;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    protected static final String TAG = "MainActivity";
    protected Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    boolean logo_on = true;
    private static final int RC_LOCATION = 1;
    private EditText editText;
    private GoogleMap mMap;
    private Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //logo animation start
        //not tracking anymore.
        
        if(logo_on){
            logo_on = false;
            Intent start_intent = new Intent(this, imageAct.class);
            startActivity(start_intent);
        }



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLastLocation();
        Button btn = (Button)findViewById(R.id.bt2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geocoder = new Geocoder(getApplicationContext(), Locale.KOREA);
                editText = (EditText)findViewById(R.id.search);
                try {
                    List<Address> addresses = geocoder.getFromLocationName(editText.getText().toString(), 1);
                    LatLng latlng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                    TextView textView = (TextView)findViewById(R.id.textView);
                    String name = addresses.get(0).getLatitude()+",";
                    name +=addresses.get(0).getLongitude();
                    textView.setText(name);
                    mMap.addMarker(new MarkerOptions().position(latlng).title("Marker in" + editText.getText().toString()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));


                } catch (IOException e) {
                    Log.e(getClass().toString(), "Failed in using Geocoder.", e);
                    return;
                }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latlng= new LatLng(10, 10);
        //mMap.addMarker(new MarkerOptions().position(latlng).title("Marker in"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @SuppressWarnings("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION) // automatically re-invoke this method after getting the required permission
    public void getLastLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLastLocation = task.getResult();
                                show_first_view();
                            } else {
                                Log.w(TAG, "getLastLocation:exception", task.getException());
                                //showSnackbar(getString(R.string.no_location_detected));
                            }
                        }
                    });
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this,
                    "This app needs access to your location to know where you are.",
                    RC_LOCATION, perms);
        }
    }

    public void show_first_view() {

        geocoder = new Geocoder(this, Locale.KOREA);
        try {
            List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
            LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            mMap.addMarker(new MarkerOptions().position(latlng).title("Marker in" + addresses.get(0).toString()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        } catch (IOException e) {
            Log.e(getClass().toString(), "Failed in using Geocoder.", e);
            return;
        }

    }


}
