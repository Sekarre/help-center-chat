package com.sekarre.helpcenterchat.services.chat.security;


import com.sekarre.helpcenterchat.domain.User;

public interface ChatAuthorizationService {

    boolean checkIfUserAuthorizedToJoinChannel(String channelId);
    boolean checkIfUserAuthorizedToJoinChannel(User user, String channelId);
}
