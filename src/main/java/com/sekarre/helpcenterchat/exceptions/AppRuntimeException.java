package com.sekarre.helpcenterchat.exceptions;

public abstract class AppRuntimeException extends RuntimeException {

    public AppRuntimeException(String message) {
        super(message);
    }
}
