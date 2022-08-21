package com.sekarre.helpcenterchat.services.security;


import com.sekarre.helpcenterchat.domain.User;

public interface ChatAuthorizationService {

    boolean checkIfUserAuthorizedToJoinChannel(String channelId);
    boolean checkIfUserAuthorizedToJoinChannel(User user, String channelId);
}
