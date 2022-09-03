package com.sekarre.helpcenterchat.factories;

import com.sekarre.helpcenterchat.DTO.ChatCreateRequestDTO;
import com.sekarre.helpcenterchat.DTO.ChatInfoDTO;
import com.sekarre.helpcenterchat.domain.Chat;
import com.sekarre.helpcenterchat.domain.Issue;

import java.util.Collections;

import static com.sekarre.helpcenterchat.factories.UserMockFactory.getCurrentUserMock;
import static com.sekarre.helpcenterchat.factories.UserMockFactory.getDefaultUserMock;
import static com.sekarre.helpcenterchat.util.DateUtil.getCurrentDateTime;

public class ChatMockFactory {

    public static ChatInfoDTO getChatInfoDTOMock() {
        return ChatInfoDTO.builder()
                .id(1L)
                .channelId("randomChannelId")
                .issueId(1L)
                .channelName("ChannelName")
                .build();
    }

    public static ChatCreateRequestDTO getChatCreateRequestDTOMock() {
        return ChatCreateRequestDTO.builder()
                .channelName("ChannelName")
                .usersId(new Long[]{1L})
                .build();
    }

    public static Chat getChatWithCurrentUserMock() {
        return Chat.builder()
                .id(1L)
                .users(Collections.singletonList(getCurrentUserMock()))
                .adminUser(getCurrentUserMock())
                .channelName("ChannelName")
                .channelId("ChannelId")
                .createdAt(getCurrentDateTime())
                .build();
    }

    public static Chat getChatWithDefaultUserMock() {
        return Chat.builder()
                .id(1L)
                .users(Collections.singletonList(getDefaultUserMock()))
                .adminUser(getDefaultUserMock())
                .channelName("ChannelName")
                .channelId("ChannelId")
                .issue(Issue.builder().id(1L).build())
                .createdAt(getCurrentDateTime())
                .build();
    }
}
