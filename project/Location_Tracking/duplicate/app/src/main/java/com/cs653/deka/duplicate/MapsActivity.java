
        package com.cs653.deka.duplicate;

        import android.Manifest;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Address;
        import android.location.Geocoder;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;

        import com.google.android.gms.maps.CameraUpdate;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;

        import java.io.IOException;
        import java.util.List;
        import java.util.StringTokenizer;

        public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double Lat, Lon;


    public String raw_msg;

    Button btsearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        raw_msg = intent.getStringExtra("msg");




    }

    public void onSearch(View view) {
        EditText location_tf = (EditText) findViewById(R.id.edLoc);
        String location = location_tf.getText().toString();

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addresses.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
         mMap = googleMap;

        // Add a marker in Sydney and move the camera

//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//        mMap.animateCamera(CameraUpdateFactory.zoomIn());

        process_raw_msg(raw_msg);

        Log.i("current_location = ",Double.toString(Lat)+" "+Double.toString(Lon));

        LatLng his_loc = new LatLng(Lat, Lon);

        //CameraUpdate center = CameraUpdateFactory.newLatLng(his_loc);

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(30);
        mMap.addMarker(new MarkerOptions().position(his_loc).title("Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(his_loc));

        // mMap.moveCamera(center);
        mMap.animateCamera(zoom);

    }

    public void process_raw_msg(String msg) {
        StringTokenizer st = new StringTokenizer(msg);
        for(int i = 0;st.hasMoreTokens();i++) {
            String temp = st.nextToken();
            Log.i("token : ",temp);
            if(i == 2) {
                Lat = Float.parseFloat(temp);
            }else if(i == 5) {
                Lon = Float.parseFloat(temp);
            }
        }
    }

}