package com.example.deka.locationapp;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class LiveTracking extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String email;
    DatabaseReference locations;
    Double lat,lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        locations = FirebaseDatabase.getInstance().getReference("Locations");
        if(getIntent() != null) {
            email = getIntent().getStringExtra("email");
            lat = getIntent().getDoubleExtra("lat",0);
            lng = getIntent().getDoubleExtra("lng",0);
        }
        if(!TextUtils.isEmpty(email)) {
            loadLocationForThisUser(email);

        }
    }

    private void loadLocationForThisUser(String email) {

        Query user_location = locations.orderByChild("email").equalTo(email);
        user_location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()) {

                    Tracking tracking = postSnapshot.getValue(Tracking.class);

                    LatLng f_loc = new LatLng(Double.parseDouble(tracking.getLat()),
                            Double.parseDouble(tracking.getLng()));

                    Location current_user = new Location("");
                    current_user.setLatitude(lat);
                    current_user.setLongitude(lng);

                    Location ffrnd = new Location("");
                    ffrnd.setLatitude(Double.parseDouble(tracking.getLat()));
                    ffrnd.setLongitude(Double.parseDouble(tracking.getLng()));

                    //calculate_distance(current_user,ffrnd);

                    mMap.addMarker(new MarkerOptions()
                                    .position(f_loc)
                                    .title(tracking.getEmail())
                                    .snippet("Dist : "+new DecimalFormat("#.#").format(calculate_distance(current_user,ffrnd)))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    );
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),12.0f));

                }

                LatLng current  = new LatLng(lat,lng);
                mMap.addMarker(new MarkerOptions().position(current).title(FirebaseAuth.getInstance().getCurrentUser().getEmail()));

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private double calculate_distance(Location current_user, Location ffrnd) {
        double theta = current_user.getLongitude() - ffrnd.getLongitude();
        double dist = Math.sin(deg2rad(current_user.getLatitude()))
                    *Math.sin(deg2rad(ffrnd.getLatitude()))
                    *Math.cos(deg2rad(current_user.getLatitude()))
                    *Math.cos(deg2rad(ffrnd.getLatitude()))
                    *Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }


    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override




    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

/*        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
}
