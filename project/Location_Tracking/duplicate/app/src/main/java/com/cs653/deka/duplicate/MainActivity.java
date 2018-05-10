package com.cs653.deka.duplicate;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    Button getLocationBtn;
    TextView locationText;
    private static final int PERMISSION_ALL     =   1;

    int i = 0;
    LocationManager locationManager;

    public int perm_access[] = {};
    private String phoneNo;
    private String message;
    private static final String TAG = MainActivity.class.getSimpleName() ;
    private final String  BROADCAST_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private IntentFilter intentFilter;

    private final String pattern_1 = "Lat";
    private final String pattern_2 = "Lon";

    public double Lat,Lon;
    public String currect_location;


    SmsBroadcastReceiver mSmsBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();

        setContentView(R.layout.activity_main);




        // locationText.setMovementMethod(new ScrollingMovementMethod());

        String[] PERMISSIONS = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        mSmsBroadcastReceiver = new SmsBroadcastReceiver();

        if(!hasPermissions(this, PERMISSIONS)){


            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);


        }else {

            // create the smslister again for the broadcast object

            mSmsBroadcastReceiver.bindListener_for_main_activity(new SmsListener() {
                @Override
                public void messageReceived(String sender, String messageText) {
                    Log.i("Text", messageText);
                    Toast.makeText(MainActivity.this, "Message: " + messageText, Toast.LENGTH_LONG).show();
                }
            });



        }


        //set onclick listener for the button
/*        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });*/

        //deka's code





        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);


        ImageButton send_button = (ImageButton) findViewById(R.id.send_button);

        final EditText phone_number = (EditText)findViewById(R.id.contact_field);

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "GML"; // get the geo location and prepare the string
                Log.i("phone_number = ",phone_number.getText().toString().trim());
                String pno = phone_number.getText().toString().trim();
                sendSMSMessage(pno, msg);
            }
        });


        // fire the service from this location
/*
        Intent myIntent = new Intent(MainActivity.this, MyService.class);
        startActivity(myIntent);
*/




        //Intent serviceIntent = new Intent();
/*        serviceIntent.setAction("com.testApp.service.MY_SERVICE");*/
        // serviceIntent.setAction("com.cs653.deka.duplicate.MyService");
        //Intent serviceIntent = new Intent("com.cs653.deka.duplicate.MyService");
        // startService(serviceIntent);
        // serviceIntent.setPackage("com.cs653.deka.duplicate");
        Log.i("onCreate is called","a new service is going to create");
        Intent serviceIntent = new Intent(this,MyService.class);
        Intent startIntent = new Intent(getApplicationContext(), MyService.class);
        startIntent.setAction("hello");
        startService(startIntent);


        //


    }


    // https://code.tutsplus.com/tutorials/android-fundamentals-intentservice-basics--mobile-6183
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



    //my methods
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 0, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Lat = location.getLatitude();
        Lon = location.getLongitude();
        Log.i("updated !!","wowoowowoowo" + Double.toString(Lat) + " " + Double.toString(Lon));

        try {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            currect_location = "\n"+addresses.get(0).getAddressLine(0)+", "+ addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2);

        }catch(Exception e)  {

        }

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




    
    protected void sendSMSMessage(String pno,String msg) {

        phoneNo = pno;
        message = msg;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // when permission was not there !
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                Log.i("hi there","");
            } else {
                /*ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);*/
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_ALL);
            }
        }else {
            sendSMS(phoneNo,message);
        }

    }


    // https://android.stackexchange.com/questions/111448/how-can-i-keep-an-app-not-made-by-me-running-in-the-background-continuously
    // https://forums.oneplus.net/threads/how-to-keep-a-particular-app-alive-in-background.491706/
    // https://www.quora.com/How-do-I-run-a-service-continuously-in-android
    //
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {

        Log.i("imppppppp","invoked !!");
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL: {
                Log.i(permissions[i],Integer.toString(grantResults[i]));
                mSmsBroadcastReceiver.bindListener_for_main_activity(new SmsListener() {
                    @Override
                    public void messageReceived(String sender, String messageText) {


                        Log.i("Text", messageText);
                        Toast.makeText(MainActivity.this, "Message: " + messageText, Toast.LENGTH_LONG).show();

                        int type_of_msg = check_message(messageText);
                        switch(type_of_msg) {
                            // this is for query location
                            case -1: {

                                messageText = "Lat = " +
                                        Double.toString(Lat) + "\n" +
                                        "Lon = " +
                                        Double.toString(Lon) + "\n" +
                                        "location = " + currect_location;

                                sendSMS(sender, messageText);

                                break;

                            }
                            // this is for valid map
                            case 1: {

                                Intent map_intent = new Intent(MainActivity.this,MapsActivity.class);
                                map_intent.putExtra("msg",messageText);
                                startActivity(map_intent);

                                break;
                            }
                        }
                    }
                });
            }
            default:
                Log.i("default case :","Nothing happended actually");
        }

    }

    int check_message(String message) {
        if( message.contains(pattern_1) )
            return 1;
        else
            return -1;
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        // important


        registerReceiver(mSmsBroadcastReceiver, intentFilter);


        Log.e("MainActivity","Registered receiver");
    }

    @Override
    protected void onStop() {
        super.onStop();

        mSmsBroadcastReceiver.set_smslistner_null_from_mainactivity();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mSmsBroadcastReceiver);
        Log.e("Main" +
                "Activity","Unregistered receiver");
    }
}
