package com.sekarre.helpcenterchat.services.chat.security.impl;

import com.sekarre.helpcenterchat.domain.Chat;
import com.sekarre.helpcenterchat.domain.User;
import com.sekarre.helpcenterchat.exceptions.chat.ChatAuthorizationException;
import com.sekarre.helpcenterchat.exceptions.chat.ChatNotFoundException;
import com.sekarre.helpcenterchat.repositories.ChatRepository;
import com.sekarre.helpcenterchat.services.chat.security.ChatAuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.sekarre.helpcenterchat.security.UserDetailsHelper.getCurrentUser;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatAuthorizationServiceImpl implements ChatAuthorizationService {

    private final ChatRepository chatRepository;

    @Override
    public boolean checkIfUserAuthorizedToJoinChannel(String channelId) {
        Chat chat = getChatByChannelIdWithUsers(channelId);
        if (!chat.getUsers().contains(getCurrentUser()) && !chat.getAdminUser().equals(getCurrentUser())) {
            throw new ChatAuthorizationException("User is not authorized to join channel with channel id: " + channelId);
        }
        return true;
    }

    @Override
    public boolean checkIfUserAuthorizedToJoinChannel(User user, String channelId) {
        if (!getChatByChannelIdWithUsers(channelId).getUsers().contains(user)) {
            throw new ChatAuthorizationException("User is not authorized to join channel with channel id: " + channelId);
        }
        return true;
    }

    private Chat getChatByChannelIdWithUsers(String channelId) {
        return chatRepository.findByChannelIdWithUsers(channelId)
                .orElseThrow(() -> new ChatNotFoundException("Chat with channel id: " + channelId + " not found"));
    }
}
