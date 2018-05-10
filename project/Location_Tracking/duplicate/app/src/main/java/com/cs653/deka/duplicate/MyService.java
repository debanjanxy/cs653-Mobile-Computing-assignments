package com.cs653.deka.duplicate;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

/**
 * Created by deka on 4/3/18.
 */

public class MyService extends Service implements LocationListener{

    public SmsBroadcastReceiver back_ground_process;
    private IntentFilter intentFilter;
    static final int NOTIFICATION_ID = 543;
    private final String  BROADCAST_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static boolean isServiceRunning = false;
    LocationManager locationManager;

    public double Lat,Lon;
    public String currect_location;


    private final String pattern_1 = "Lat";
    private final String pattern_2 = "Lon";



    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("onCreate","good_0");
        back_ground_process = new SmsBroadcastReceiver();

        back_ground_process.bindListener_for_service(new sms_listener_for_service() {
            @Override
            public void messageReceived(String sender, String messageText) {
                // take it and give it to the location module
/*                Toast.makeText(MyService.this, "Message: " + messageText + sender, Toast.LENGTH_LONG).show();

                // send the location co ordinate  to the sender again

                // get the locaiton now !

                messageText = "Lat = " +
                                Double.toString(Lat) + "\n" +
                                "Lon = " +
                                Double.toString(Lon) + "\n" +
                                "location = " + currect_location;


                sendSMS(sender, messageText);*/

                Log.i("Text", messageText);
                Toast.makeText(MyService.this, "Message: " + messageText, Toast.LENGTH_LONG).show();

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

                        Intent map_intent = new Intent(MyService.this,MapsActivity.class);
                        map_intent.putExtra("msg",messageText);
                        startActivity(map_intent);

                        break;
                    }
                }

            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);

        registerReceiver(back_ground_process, intentFilter);
        Log.e("Service","Registered receiver Done !");

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 0, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }

        startServiceWithNotification();
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
            Toast.makeText(MyService.this, "Message Sent",  Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(MyService.this,ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    // on click on the notification bar
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("onStartCommand","good_1");
        if (intent != null && intent.getAction().equals("hello")) {
            Log.i("again !!","good_5");
            startServiceWithNotification();
        }
        else stopMyService();

        // imp
        return START_STICKY;
    }
    // In case the service is deleted or crashes some how
    @Override
    public void onDestroy() {
        isServiceRunning = false;
        Log.i("killed","service is getting closed !");
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        Log.i("onBind","good_x");
        return null;
    }



    // notification code
    void startServiceWithNotification() {
        if (isServiceRunning)  {
            Log.i("returned","good_3");
            return;
        }
        isServiceRunning = true;
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setAction("hello");  // A string containing the action name
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_directions_bike_black_24dp);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.my_string))
                .setSmallIcon(R.drawable.ic_directions_bike_black_24dp)
                //.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(contentPendingIntent)
                .setOngoing(true)
//                .setDeleteIntent(contentPendingIntent)  // if needed
                .build();
        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        Log.i("Foreground started","good_4");
        startForeground(NOTIFICATION_ID, notification);
    }
    void stopMyService() {
        stopForeground(true);
        stopSelf();
        isServiceRunning = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        //locationText.append("\n-------\n"+i+". Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude()+"\n-------");
        //i = i+1;

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
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
