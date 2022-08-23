package com.sekarre.helpcenterchat.exceptions.notification;

import com.sekarre.helpcenterchat.exceptions.AppRuntimeException;

public class NotificationSendFailedException extends AppRuntimeException {

    public NotificationSendFailedException(String message) {
        super(message);
    }
}
