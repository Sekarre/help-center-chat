package com.sekarre.helpcenterchat.factories;


import com.sekarre.helpcenterchat.domain.Role;
import com.sekarre.helpcenterchat.domain.User;

import java.util.Set;

public class UserMockFactory {

    public static User getDefaultUserMock() {
        return User.builder()
                .id(1L)
                .username("Default User")
                .password("password")
                .build();
    }

    public static User getCurrentUserMock() {
        return User.builder()
                .id(2L)
                .username("Current User")
                .password("password")
                .build();
    }

    public static User getUserWithRolesMock(Set<Role> roles) {
        return User.builder()
                .id(3L)
                .roles(roles)
                .username("Current User")
                .password("password")
                .build();
    }
}