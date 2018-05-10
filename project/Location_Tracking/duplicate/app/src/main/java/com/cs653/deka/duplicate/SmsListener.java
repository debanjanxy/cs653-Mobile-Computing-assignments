package com.cs653.deka.duplicate;

/**
 * Created by deka on 4/3/18.
 */

public interface SmsListener {
    void messageReceived(String sender, String messageText);
}
