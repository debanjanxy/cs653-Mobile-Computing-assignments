package com.example.debanjan.accidentdetector;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener{

    public CheckBox accelerometer = (CheckBox)findViewById(R.id.acc);
    public CheckBox gps = (CheckBox)findViewById(R.id.gps);
    public Switch start = (Switch)findViewById(R.id.start);
    public TextView textView = (TextView)findViewById(R.id.textView);
    private static final int PERMISSION_ALL     = 1;

    int i = 0;
    LocationManager locationManager;

    public int perm_access[] = {};
    private String phoneNo;
    private String message;
    private static final String TAG = MainActivity.class.getSimpleName() ;
    private final String  BROADCAST_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView.setMovementMethod(new ScrollingMovementMethod()); // for text view scrolling
        String[] PERMISSIONS = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 0, (LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        textView.append("\n-------\n"+i+". Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude()+"\n-------");
        i = i+1;
        Log.w("Latitude: ", Double.toString(location.getLatitude()));
        Log.w("Longitude: ", Double.toString(location.getLongitude()));
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

}
