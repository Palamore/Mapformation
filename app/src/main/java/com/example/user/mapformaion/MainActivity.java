package com.example.user.mapformaion;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    protected static final String TAG = "MainActivity";
    boolean logo_on = true;
    protected Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int RC_LOCATION = 1;
    private static final int RC_LOCATION_UPDATE = 2;
    private EditText editText;
    private GoogleMap mMap;
    private Geocoder geocoder;
    final Context context = this;
    Intent intent;
    String gUrl = "https://www.google.co.kr/search?q=";
    String nUrl = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=";
    String wUrl = "https://namu.wiki/w/";
    String marker_str;
    String gCnt = "gCount";
    String nCnt = "nCount";
    String wCnt = "wCount";
    String Svalue;
    Vector cnt_value = new Vector();

    String isthis = "True";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(logo_on){
            logo_on = false;
            Intent start_intent = new Intent(this, imageAct.class);
            startActivity(start_intent);
        }

        intent = new Intent(this, WebActivity.class);

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
                marker_str = editText.getText().toString();
                try {
                    List<Address> addresses = geocoder.getFromLocationName(editText.getText().toString(), 1);
                    LatLng latlng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                    String name = addresses.get(0).getLatitude()+",";
                    name +=addresses.get(0).getLongitude();
                    mMap.addMarker(new MarkerOptions().position(latlng).title("Marker in " + editText.getText().toString()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));


                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Locations");


                    Query locate = myRef.child(marker_str).orderByChild(gCnt).limitToFirst(10);
                    locate.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                            Svalue = dataSnapshot.getValue().toString();
                        }catch(NullPointerException e){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Locations");
                            String key = marker_str;
                            myRef.child(key).child(gCnt).setValue(0);
                            myRef.child(key).child(nCnt).setValue(0);
                            myRef.child(key).child(wCnt).setValue(0);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    Query locate2 = myRef.child(marker_str).orderByChild(gCnt).limitToFirst(10);
                    locate2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            cnt_value.clear();
                            for(DataSnapshot cntVal: dataSnapshot.getChildren()) {
                                String gVal = cntVal.getValue().toString();
                                cnt_value.add(gVal);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


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
        mMap.addMarker(new MarkerOptions().position(latlng).title("Marker in"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.setOnMarkerClickListener(this);

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

            mMap.addMarker(new MarkerOptions().position(latlng).title("Marker in " + addresses.get(0).toString()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

            Toast.makeText(this,isthis,Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.e(getClass().toString(), "Failed in using Geocoder.", e);
            return;
        }

    }


    @Override
    public boolean onMarkerClick(Marker marker) {



        AlertDialog.Builder selectDialogBuilder = new AlertDialog.Builder(
                context);
        selectDialogBuilder
                .setTitle("해당 키워드를 검색")
                .setMessage("어디서 검색할까요?")
                .setPositiveButton("Google에 검색" + cnt_value.elementAt(0),
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                intent.putExtra("Param1", gUrl + marker_str);
                                intent.putExtra("Param2", gCnt);
                                intent.putExtra("Param3", marker_str);
                                intent.putExtra("Param4", (String)cnt_value.elementAt(0));
                                startActivity(intent);

                            }
                        })
                .setNegativeButton("Naver에 검색" + cnt_value.elementAt(1),
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {

                                intent.putExtra("Param1", nUrl + marker_str);
                                intent.putExtra("Param2", nCnt);
                                intent.putExtra("Param3", marker_str);
                                intent.putExtra("Param4", (String)cnt_value.elementAt(1));
                                startActivity(intent);

                            }
                        })
                .setNeutralButton("나무위키에 검색" + cnt_value.elementAt(2),
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {

                                intent.putExtra("Param1", wUrl + marker_str);
                                intent.putExtra("Param2", wCnt);
                                intent.putExtra("Param3", marker_str);
                                intent.putExtra("Param4", (String)cnt_value.elementAt(2));
                                startActivity(intent);

                            }
                        })


;
        AlertDialog selectDialog = selectDialogBuilder.create();
        selectDialog.show();


        return false;
    }
}
