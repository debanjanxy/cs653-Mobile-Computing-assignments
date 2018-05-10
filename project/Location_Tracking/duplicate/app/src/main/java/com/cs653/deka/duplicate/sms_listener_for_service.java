package com.cs653.deka.duplicate;

/**
 * Created by deka on 5/3/18.
 */

public interface sms_listener_for_service {
    void messageReceived(String sender, String messageText);
}
