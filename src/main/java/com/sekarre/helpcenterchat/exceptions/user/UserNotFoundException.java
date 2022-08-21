package com.sekarre.helpcenterchat.exceptions.user;


import com.sekarre.helpcenterchat.exceptions.AppRuntimeException;

public class UserNotFoundException extends AppRuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
