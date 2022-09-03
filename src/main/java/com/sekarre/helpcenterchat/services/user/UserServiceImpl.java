package com.sekarre.helpcenterchat.services.user;

import com.sekarre.helpcenterchat.domain.User;
import com.sekarre.helpcenterchat.exceptions.user.UserNotFoundException;
import com.sekarre.helpcenterchat.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getUsersByIds(Long[] usersIds) {
        List<User> users = new ArrayList<>(usersIds.length);
        for (Long userId : usersIds) {
            users.add(getUserById(userId));
        }
        return users;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found"));
    }
}
