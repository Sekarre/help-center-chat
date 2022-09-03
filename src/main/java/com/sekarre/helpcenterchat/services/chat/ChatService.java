package com.sekarre.helpcenterchat.services.chat;


import com.sekarre.helpcenterchat.DTO.ChatCreateRequestDTO;
import com.sekarre.helpcenterchat.DTO.ChatInfoDTO;
import com.sekarre.helpcenterchat.DTO.ChatMessageDTO;

import java.util.List;

public interface ChatService {


    ChatInfoDTO createNewChatWithUsers(ChatCreateRequestDTO chatCreateRequestDTO);

    void joinChat(String channelId);

    ChatMessageDTO savePrivateChatMessage(ChatMessageDTO chatMessageDTO, String channelId);

    List<ChatMessageDTO> getAllChatMessages(String channelId);

    List<ChatInfoDTO> getChatChannels();
}
