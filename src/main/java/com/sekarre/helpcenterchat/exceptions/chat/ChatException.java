package com.sekarre.helpcenterchat.exceptions.chat;


import com.sekarre.helpcenterchat.exceptions.AppRuntimeException;

public abstract class ChatException extends AppRuntimeException {
    public ChatException(String message) {
        super(message);
    }
}
