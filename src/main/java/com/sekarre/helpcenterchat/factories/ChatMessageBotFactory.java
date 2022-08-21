package com.sekarre.helpcenterchat.factories;


import com.sekarre.helpcenterchat.DTO.ChatMessageDTO;

import static com.sekarre.helpcenterchat.util.DateUtil.getCurrentDateTime;

public class ChatMessageBotFactory {

    public static final String botName = "ChatInfo";
    public static final Long botId = -1L;

    public static ChatMessageDTO getWelcomeChatMessage(String user) {
        return ChatMessageDTO.builder()
                .message(user + " joined chat")
                .senderName(botName)
                .senderId(botId)
                .createdDateTime(getCurrentDateTime())
                .build();
    }

    public static ChatMessageDTO getGoodbyeChatMessage(String user) {
        return ChatMessageDTO.builder()
                .message(user + " left chat")
                .senderName(botName)
                .senderId(botId)
                .createdDateTime(getCurrentDateTime())
                .build();
    }
}
