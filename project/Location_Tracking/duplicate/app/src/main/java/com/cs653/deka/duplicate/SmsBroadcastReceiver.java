package com.cs653.deka.duplicate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.os.Bundle;

/**
 * Created by deka on 4/3/18.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private SmsListener smsListener = null;
    private sms_listener_for_service sms_listener_for_service;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BROADCAST RECEIVER", "onReceive called()");
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0;i<pdus.length;i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String messageBody = smsMessage.getMessageBody();

            Log.i("hello","dddd");
            if(smsListener != null) {
                smsListener.messageReceived(sender, messageBody);
                messageBody = "DONT";
                //sms_listener_for_service.messageReceived(sender, messageBody);
            }
            else {

                sms_listener_for_service.messageReceived(sender,messageBody);
            }
        }

    }

    void bindListener_for_main_activity(SmsListener listener) {
        Log.i("omg","gggg");
        smsListener = listener;
    }
    void bindListener_for_service(sms_listener_for_service listener) {
        Log.i("xxxxxxxxx","dddddddddddddddddddd");
        sms_listener_for_service = listener;
    }

    void set_smslistner_null_from_mainactivity() {
        smsListener = null;
    }

    //throw new UnsupportedOperationException("Not yet implemented");
}
