package com.sekarre.helpcenterchat.services.notification.security;

import com.sekarre.helpcenterchat.SecurityContextMockSetup;
import com.sekarre.helpcenterchat.domain.Chat;
import com.sekarre.helpcenterchat.domain.User;
import com.sekarre.helpcenterchat.exceptions.chat.ChatAuthorizationException;
import com.sekarre.helpcenterchat.repositories.ChatRepository;
import com.sekarre.helpcenterchat.services.chat.security.ChatAuthorizationService;
import com.sekarre.helpcenterchat.services.chat.security.impl.ChatAuthorizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.sekarre.helpcenterchat.factories.ChatMockFactory.getChatWithCurrentUserMock;
import static com.sekarre.helpcenterchat.factories.ChatMockFactory.getChatWithDefaultUserMock;
import static com.sekarre.helpcenterchat.factories.UserMockFactory.getCurrentUserMock;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatAuthorizationServiceTest extends SecurityContextMockSetup {

    @Mock
    private ChatRepository chatRepository;

    private ChatAuthorizationService chatAuthorizationService;
    private static final String channelId = "ChannelId";

    @BeforeEach
    void setUp() {
        super.setUpSecurityContext();
        MockitoAnnotations.openMocks(this);
        chatAuthorizationService = new ChatAuthorizationServiceImpl(chatRepository);
    }


    @Test
    void should_check_if_CURRENT_user_is_authorized_to_join_channel_and_return_true() {
        //given
        final Chat chat = getChatWithCurrentUserMock();
        when(chatRepository.findByChannelIdWithUsers(any(String.class))).thenReturn(Optional.ofNullable(chat));

        //when
        boolean result = chatAuthorizationService.checkIfUserAuthorizedToJoinChannel(channelId);

        //then
        assertTrue(result);
        verify(chatRepository, times(1)).findByChannelIdWithUsers(channelId);
    }

    @Test
    void should_check_if_CURRENT_user_is_authorized_to_join_channel_and_throw_chat_authorization_exception() {
        //given
        final Chat chat = getChatWithDefaultUserMock();
        when(chatRepository.findByChannelIdWithUsers(any(String.class))).thenReturn(Optional.ofNullable(chat));

        //when
        assertThrows(ChatAuthorizationException.class, () -> chatAuthorizationService.checkIfUserAuthorizedToJoinChannel(channelId));

        //then
        verify(chatRepository, times(1)).findByChannelIdWithUsers(channelId);
    }

    @Test
    void should_check_if_GIVEN_user_is_authorized_to_join_channel_and_return_true() {
        //given
        final User user = getCurrentUserMock();
        final Chat chat = getChatWithCurrentUserMock();
        when(chatRepository.findByChannelIdWithUsers(any(String.class))).thenReturn(Optional.ofNullable(chat));

        //when
        boolean result = chatAuthorizationService.checkIfUserAuthorizedToJoinChannel(user, channelId);

        //then
        assertTrue(result);
        verify(chatRepository, times(1)).findByChannelIdWithUsers(channelId);
    }

    @Test
    void should_check_if_GIVEN_user_is_authorized_to_join_channel_and_throw_chat_authorization_exception() {
        //given
        final User user = getCurrentUserMock();
        final Chat chat = getChatWithDefaultUserMock();
        when(chatRepository.findByChannelIdWithUsers(any(String.class))).thenReturn(Optional.ofNullable(chat));

        //when
        assertThrows(ChatAuthorizationException.class, () -> chatAuthorizationService.checkIfUserAuthorizedToJoinChannel(user, channelId));

        //then
        verify(chatRepository, times(1)).findByChannelIdWithUsers(channelId);
    }
}