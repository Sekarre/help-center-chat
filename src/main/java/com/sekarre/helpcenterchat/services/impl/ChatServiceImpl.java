package com.sekarre.helpcenterchat.services.impl;

import com.sekarre.helpcenterchat.DTO.ChatCreateRequestDTO;
import com.sekarre.helpcenterchat.DTO.ChatInfoDTO;
import com.sekarre.helpcenterchat.DTO.ChatMessageDTO;
import com.sekarre.helpcenterchat.DTO.notification.NotificationQueueDTO;
import com.sekarre.helpcenterchat.domain.Chat;
import com.sekarre.helpcenterchat.domain.ChatMessage;
import com.sekarre.helpcenterchat.domain.User;
import com.sekarre.helpcenterchat.domain.enums.EventType;
import com.sekarre.helpcenterchat.exceptions.chat.ChatNotFoundException;
import com.sekarre.helpcenterchat.mappers.ChatMapper;
import com.sekarre.helpcenterchat.mappers.ChatMessageMapper;
import com.sekarre.helpcenterchat.repositories.ChatMessageRepository;
import com.sekarre.helpcenterchat.repositories.ChatRepository;
import com.sekarre.helpcenterchat.services.ChatService;
import com.sekarre.helpcenterchat.services.UserService;
import com.sekarre.helpcenterchat.services.notification.NotificationSender;
import com.sekarre.helpcenterchat.util.RandomStringGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.sekarre.helpcenterchat.security.UserDetailsHelper.getCurrentUser;
import static com.sekarre.helpcenterchat.util.DateUtil.getCurrentDateTimeFormatted;


@RequiredArgsConstructor
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRepository chatRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final ChatMapper chatMapper;
    private final UserService userService;
    private final NotificationSender notificationSender;


    @Override
    public ChatInfoDTO createNewChatWithUsers(ChatCreateRequestDTO chatCreateRequestDTO) {
        return chatMapper.mapChatToChatInfoDTO(chatRepository.save(Chat.builder()
                .adminUser(getCurrentUser())
                .users(userService.getUsersByIds(chatCreateRequestDTO.getUsersId()))
                .channelId(getUniqueChannelId())
                .channelName(chatCreateRequestDTO.getChannelName())
                .build()));
    }

    @Override
    public void joinChat(String channelId) {
        Chat chat = getChatByChannelId(channelId);
        chat.addUser(getCurrentUser());
        chatRepository.save(chat);
    }

    @Override
    public ChatMessageDTO savePrivateChatMessage(ChatMessageDTO chatMessageDTO, String channelId) {
        log.debug("User id: " + getCurrentUser().getId() + " message: " + chatMessageDTO.getMessage());
        ChatMessage chatMessage = createNewChatMessage(chatMessageDTO, channelId);
        ChatMessageDTO returnChatMessageDTO = chatMessageMapper.mapMessageToChatMessageDTO(chatMessageRepository.save(chatMessage));
        sendNewChatMessageNotification(channelId, chatMessage);
        return returnChatMessageDTO;
    }

    private void sendNewChatMessageNotification(String channelId, ChatMessage chatMessage) {
        List<Long> toSendUsersIds = chatMessage.getChat().getUsers().stream()
                .map(User::getId)
                .filter(id -> !id.equals(getCurrentUser().getId()))
                .toList();
        toSendUsersIds.forEach(userId -> sendNotification(channelId, userId));
    }

    private void sendNotification(String channelId, Long userId) {
        notificationSender.sendNotification(NotificationQueueDTO.builder()
                .eventType(EventType.NEW_CHAT_MESSAGE.name())
                .destinationId(channelId)
                .createdAt(getCurrentDateTimeFormatted())
                .userId(userId)
                .build());
    }

    private ChatMessage createNewChatMessage(ChatMessageDTO chatMessageDTO, String channelId) {
        ChatMessage chatMessage = chatMessageMapper.mapChatMessageDTOToMessage(chatMessageDTO);
        Chat chat = getChatByChannelIdWithUsers(channelId);
        chatMessage.setChat(chat);
        chatMessage.setSender(getCurrentUser());
        return chatMessage;
    }

    @Override
    public List<ChatMessageDTO> getAllChatMessages(String channelId) {
        Chat chat = getChatByChannelId(channelId);
        return chatMessageRepository.findAllByChatIdOrderByCreatedDateTime(chat.getId()).stream()
                .map(chatMessageMapper::mapMessageToChatMessageDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatInfoDTO> getChatChatInfo() {
        return chatRepository.findAll().stream()
                .map(chatMapper::mapChatToChatInfoDTO)
                .collect(Collectors.toList());
    }

    private String getUniqueChannelId() {
        String generatedChannelId = RandomStringGeneratorUtil.getRandomString();
        if (chatRepository.existsByChannelId(generatedChannelId)) {
            return getUniqueChannelId();
        }
        return generatedChannelId;
    }

    private Chat getChatByChannelId(String channelId) {
        return chatRepository.findByChannelId(channelId)
                .orElseThrow(() -> new ChatNotFoundException("Chat with channel id: " + channelId + " not found"));
    }

    private Chat getChatByChannelIdWithUsers(String channelId) {
        return chatRepository.findByChannelIdWithUsers(channelId)
                .orElseThrow(() -> new ChatNotFoundException("Chat with channel id: " + channelId + " not found"));
    }
}
