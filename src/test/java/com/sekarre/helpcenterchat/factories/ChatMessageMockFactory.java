package com.sekarre.helpcenterchat.factories;

import com.sekarre.helpcenterchat.DTO.ChatMessageDTO;
import com.sekarre.helpcenterchat.domain.Chat;
import com.sekarre.helpcenterchat.domain.ChatMessage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.sekarre.helpcenterchat.factories.UserMockFactory.getDefaultUserMock;
import static com.sekarre.helpcenterchat.util.DateUtil.DATE_TIME_FORMAT;
import static com.sekarre.helpcenterchat.util.DateUtil.getCurrentDateTimeFormatted;
import static java.time.LocalDateTime.parse;

public class ChatMessageMockFactory {

    public static ChatMessageDTO getChatMessageDTOMock() {
        return ChatMessageDTO.builder()
                .message("Message")
                .file("File")
                .createdDateTime(parse(getCurrentDateTimeFormatted(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .senderId(1L)
                .senderName("Name")
                .senderLastname("LastName")
                .build();
    }

    public static ChatMessage getChatMessageMock() {
        return ChatMessage.builder()
                .chat(Chat.builder().users(new ArrayList<>()).build())
                .id(1L)
                .message("Message")
                .file("File")
                .sender(getDefaultUserMock())
                .createdDateTime(parse(getCurrentDateTimeFormatted(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .build();
    }
}
