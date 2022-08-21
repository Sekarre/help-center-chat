package com.sekarre.helpcenterchat.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@PreAuthorize("hasRole('ADMIN') or @chatAuthorizationServiceImpl.checkIfUserAuthorizedToJoinChannel(#channelId)")
public @interface ChatReadPermission {
}
