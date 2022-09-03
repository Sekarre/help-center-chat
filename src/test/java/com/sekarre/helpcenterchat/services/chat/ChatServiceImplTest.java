package com.sekarre.helpcenterchat.services.chat;

import com.sekarre.helpcenterchat.DTO.ChatCreateRequestDTO;
import com.sekarre.helpcenterchat.DTO.ChatInfoDTO;
import com.sekarre.helpcenterchat.DTO.ChatMessageDTO;
import com.sekarre.helpcenterchat.SecurityContextMockSetup;
import com.sekarre.helpcenterchat.domain.Chat;
import com.sekarre.helpcenterchat.domain.ChatMessage;
import com.sekarre.helpcenterchat.domain.Role;
import com.sekarre.helpcenterchat.domain.User;
import com.sekarre.helpcenterchat.domain.enums.RoleName;
import com.sekarre.helpcenterchat.mappers.ChatMapper;
import com.sekarre.helpcenterchat.mappers.ChatMessageMapper;
import com.sekarre.helpcenterchat.repositories.ChatMessageRepository;
import com.sekarre.helpcenterchat.repositories.ChatRepository;
import com.sekarre.helpcenterchat.services.notification.NotificationService;
import com.sekarre.helpcenterchat.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.sekarre.helpcenterchat.factories.ChatMessageMockFactory.getChatMessageDTOMock;
import static com.sekarre.helpcenterchat.factories.ChatMessageMockFactory.getChatMessageMock;
import static com.sekarre.helpcenterchat.factories.ChatMockFactory.*;
import static com.sekarre.helpcenterchat.factories.UserMockFactory.getDefaultUserMock;
import static com.sekarre.helpcenterchat.factories.UserMockFactory.getUserWithRolesMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatServiceImplTest extends SecurityContextMockSetup {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatMessageMapper chatMessageMapper;

    @Mock
    private ChatMapper chatMapper;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    private ChatService chatService;
    private static final String channelId = "ChannelId";

    @BeforeEach
    void setUp() {
        super.setUpSecurityContext();
        MockitoAnnotations.openMocks(this);
        chatService = new ChatServiceImpl(chatMessageRepository, chatRepository, chatMessageMapper, chatMapper,
                userService, notificationService);
    }

    @Test
    void should_create_new_chat_with_users_and_return_chat_info() {
        //given
        final ChatInfoDTO chatInfoDTO = getChatInfoDTOMock();
        final ChatCreateRequestDTO chatCreateRequestDTO = getChatCreateRequestDTOMock();
        final Chat chat = getChatWithDefaultUserMock();
        final User user = getDefaultUserMock();
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);
        when(chatMapper.mapChatToChatInfoDTO(any(Chat.class))).thenReturn(chatInfoDTO);
        when(userService.getUsersByIds(any(Long[].class))).thenReturn(Collections.singletonList(user));

        //when
        ChatInfoDTO result = chatService.createNewChatWithUsers(chatCreateRequestDTO);

        //then
        assertNotNull(result);
        assertEquals(result.getChannelName(), chatCreateRequestDTO.getChannelName(),
                "Channel name are not equal");
        verify(chatRepository, times(1)).save(any(Chat.class));
        verify(chatMapper, times(1)).mapChatToChatInfoDTO(chat);
        verify(userService, times(1)).getUsersByIds(chatCreateRequestDTO.getUsersId());
    }


    @Test
    void should_join_chat_by_adding_user() {
        //given
        final Chat chat = getChatWithCurrentUserMock();
        when(chatRepository.findByChannelId(any(String.class))).thenReturn(Optional.ofNullable(chat));

        //when
        chatService.joinChat(channelId);

        //then
        verify(chatRepository, times(1)).findByChannelId(channelId);
        verify(chatRepository, times(1)).save(chat);
    }

    @Test
    void should_save_private_chat_message_and_return_chat_message() {
        //given
        final Chat chat = getChatWithCurrentUserMock();
        final ChatMessageDTO chatMessageDTO = getChatMessageDTOMock();
        final ChatMessage chatMessage = getChatMessageMock();
        when(chatMessageMapper.mapChatMessageDTOToMessage(any(ChatMessageDTO.class))).thenReturn(chatMessage);
        when(chatRepository.findByChannelIdWithUsers(any(String.class))).thenReturn(Optional.ofNullable(chat));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);
        when(chatMessageMapper.mapChatMessageToChatMessageDTO(any(ChatMessage.class))).thenReturn(chatMessageDTO);

        //when
        ChatMessageDTO result = chatService.savePrivateChatMessage(chatMessageDTO, channelId);

        //then
        assertNotNull(result);
        verify(chatMessageMapper, times(1)).mapChatMessageDTOToMessage(chatMessageDTO);
        verify(chatRepository, times(1)).findByChannelIdWithUsers(channelId);
        verify(chatMessageRepository, times(1)).save(chatMessage);
        verify(chatMessageMapper, times(1)).mapChatMessageToChatMessageDTO(chatMessage);
    }

    @Test
    void should_return_all_chat_messages() {
        //given
        final Chat chat = getChatWithCurrentUserMock();
        final ChatMessageDTO chatMessageDTO = getChatMessageDTOMock();
        final ChatMessage chatMessage = getChatMessageMock();
        when(chatRepository.findByChannelId(any(String.class))).thenReturn(Optional.ofNullable(chat));
        when(chatMessageRepository.findAllByChatIdOrderByCreatedDateTime(any(Long.class))).thenReturn(Collections.singletonList(chatMessage));
        when(chatMessageMapper.mapChatMessageToChatMessageDTO(any(ChatMessage.class))).thenReturn(chatMessageDTO);

        //when
        List<ChatMessageDTO> result = chatService.getAllChatMessages(channelId);

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(chatRepository, times(1)).findByChannelId(channelId);
        verify(chatMessageRepository, times(1)).findAllByChatIdOrderByCreatedDateTime(chat.getId());
        verify(chatMessageMapper, times(1)).mapChatMessageToChatMessageDTO(chatMessage);
    }

    @Test
    void should_return_chat_channels_for_basic_user() {
        //given
        setUpSecurityContext(getUserWithRolesMock(Collections.singleton(Role.builder().name(RoleName.BASIC).build())));
        final ChatInfoDTO chatInfoDTO = getChatInfoDTOMock();
        final Chat chat = getChatWithCurrentUserMock();
        when(chatRepository.findAllByUsersContaining(any(User.class))).thenReturn(Collections.singletonList(chat));
        when(chatMapper.mapChatToChatInfoDTO(any(Chat.class))).thenReturn(chatInfoDTO);

        //when
        List<ChatInfoDTO> result = chatService.getChatChannels();

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(chatRepository, times(1)).findAllByUsersContaining(any(User.class));
        verify(chatMapper, times(1)).mapChatToChatInfoDTO(chat);
    }

    @Test
    void should_return_chat_channels_for_admin_user() {
        //given
        setUpSecurityContext(getUserWithRolesMock(Collections.singleton(Role.builder().name(RoleName.ADMIN).build())));
        final ChatInfoDTO chatInfoDTO = getChatInfoDTOMock();
        final Chat chat = getChatWithCurrentUserMock();
        when(chatRepository.findAll()).thenReturn(Collections.singletonList(chat));
        when(chatMapper.mapChatToChatInfoDTO(any(Chat.class))).thenReturn(chatInfoDTO);

        //when
        List<ChatInfoDTO> result = chatService.getChatChannels();

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(chatRepository, times(1)).findAll();
        verify(chatMapper, times(1)).mapChatToChatInfoDTO(chat);
    }
}