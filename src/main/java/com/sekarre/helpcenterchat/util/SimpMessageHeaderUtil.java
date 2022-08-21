package com.sekarre.helpcenterchat.util;

import com.sekarre.helpcenterchat.domain.User;
import com.sekarre.helpcenterchat.exceptions.WebSocketAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.Objects;

@Slf4j
public class SimpMessageHeaderUtil {

    private static final String channelSeparatorRegex = "\\.";

    public static User getUserFromHeaders(SimpMessageHeaderAccessor headers) {
        Principal principal =  headers.getUser();
        User user = (User) (principal != null ? ((UsernamePasswordAuthenticationToken) principal).getPrincipal() : null);

        if (Objects.isNull(user)) {
            throw new WebSocketAuthenticationException("User is not authenticated -> [headers]: " + headers);
        }
        return user;
    }

    public static String getChannelIdFromDestinationHeader(String destination) {
        if (Objects.isNull(destination)) {
            return null;
        }
        String[] splitDest = destination.split(channelSeparatorRegex);
        return splitDest[splitDest.length - 1];
    }
}
