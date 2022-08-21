package com.sekarre.helpcenterchat.services;

import com.sekarre.helpcenterchat.domain.User;

import java.util.List;

public interface UserService {

    List<User> getUsersByIds(Long[] usersIds);
}
