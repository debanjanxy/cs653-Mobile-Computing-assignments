package com.example.deka.locationapp;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListOnline extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    DatabaseReference onlinereference,currnetUser,counterreference,locations;
    FirebaseRecyclerAdapter<User,ListOnlineviewHolder> adapter;


    RecyclerView listOnline;
    RecyclerView.LayoutManager layoutManager;


    // map related code

    private static final int MY_PERM_CODE = 1234;
    private static final int MY_PERM_GP_CODE = 1235;
    private LocationRequest location_Request;
    private GoogleApiClient google_ApiClient;
    private Location last_location;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISTANCE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online);

        listOnline = (RecyclerView)findViewById(R.id.listOnline);
        // listOnline.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        listOnline.setLayoutManager(layoutManager);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.setTitle("TRACK");
        setSupportActionBar(toolbar);


        locations = FirebaseDatabase.getInstance().getReference("Locations");

        onlinereference = FirebaseDatabase.getInstance().getReference().child(".info/connected");

        counterreference=FirebaseDatabase.getInstance().getReference("lastOnline");

        currnetUser = FirebaseDatabase.getInstance().getReference("lastOnline")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());




        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },MY_PERM_CODE);


        }else {
            if(checkPlayServices()) {
                buildGoolgeApiClient();
                createLocationRequest();
                displayLocation();
            }
        }




        //
        setupSystem();
        UpdateList();
    } //////////////////////////////////////////////////


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERM_CODE: {

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if(checkPlayServices()) {
                        buildGoolgeApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }

            }

            break;
        }
    }

    private void createLocationRequest() {
        location_Request = LocationRequest.create();
        location_Request.setInterval(UPDATE_INTERVAL);
        location_Request.setFastestInterval(FASTEST_INTERVAL);
        location_Request.setSmallestDisplacement(DISTANCE);
        location_Request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void displayLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else {

        }

        last_location = LocationServices.FusedLocationApi.getLastLocation(google_ApiClient);

        if(last_location != null) {
            locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue( new Tracking (

                                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                String.valueOf(last_location.getLatitude()),
                                String.valueOf(last_location.getLongitude())
                            )
                    );
        }else {






            //Toast.makeText(this,"could not get the location",Toast.LENGTH_SHORT).show();

        }
    }

    private void buildGoolgeApiClient() {
        google_ApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        google_ApiClient.connect();
    }

    private boolean checkPlayServices() {

        int resultcode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultcode != ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultcode)) {
                GooglePlayServicesUtil.getErrorDialog(resultcode,this,MY_PERM_GP_CODE).show();
            }else {
                Toast.makeText(this,"This device does not support google play services",Toast.LENGTH_SHORT).show();
                finish();
            }
            return  false;
        }


        return true;
    }



    ///////////////////////////////////////////////


    private void UpdateList() {

        adapter = new FirebaseRecyclerAdapter<User, ListOnlineviewHolder>(
                User.class,
                R.layout.user_layout,
                ListOnlineviewHolder.class,
                counterreference) {

            @Override
            protected void populateViewHolder(ListOnlineviewHolder viewHolder, final User model, int position) {
                // Log.i("calllllledddd","ggggggggg");
                viewHolder.text_email.setText(model.getEmail());

                viewHolder.item_Clicked_Listner = new itemClickedListner() {
                    @Override
                    public void onClick(View view, int position) {
                        if(!model.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            Log.i("fucked2","lol2");
                            Intent map = new Intent(ListOnline.this,LiveTracking.class);
                            map.putExtra("email",model.getEmail());
                            map.putExtra("lat",last_location.getLatitude());
                            map.putExtra("lng",last_location.getLongitude());
                            startActivity(map);
                        }else {
                            Log.i("fucked","lol1");
                        }
                    }
                };

            }

        };


        adapter.notifyDataSetChanged();
        listOnline.setAdapter(adapter);

    }

    // listerners for the firebase system

    private void setupSystem() {
        onlinereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Boolean.class)) {

                    currnetUser.onDisconnect().removeValue();

                    counterreference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(),"Online"));
                    adapter.notifyDataSetChanged();
                    Log.i("entered_1","hello_1");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        counterreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())  {

                    User user = postSnapshot.getValue(User.class);
                    Log.i("Log"," "+ user.getEmail()+" is " + user.getStatus());
                    Log.i("entered_2","hello_2");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())  {
            case R.id.action_join:
                Log.i("should be","loged IN");
                counterreference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(),"Online"));
                break;
            case R.id.action_logout:
                Log.i("should be","loged OUT");
                currnetUser.removeValue();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdate();

    }




    private void startLocationUpdate() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(google_ApiClient,location_Request,this);


    }

    @Override
    public void onConnectionSuspended(int i) {
        google_ApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        last_location  = location;
        displayLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(google_ApiClient != null) {
            google_ApiClient.connect();
        }

    }

    @Override
    protected void onStop() {
        if(google_ApiClient != null) {
            google_ApiClient.disconnect();
        }

        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();

    }
}
