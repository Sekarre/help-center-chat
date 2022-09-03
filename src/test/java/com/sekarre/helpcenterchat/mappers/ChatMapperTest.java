package com.sekarre.helpcenterchat.mappers;

import com.sekarre.helpcenterchat.DTO.ChatInfoDTO;
import com.sekarre.helpcenterchat.domain.Chat;
import com.sekarre.helpcenterchat.testutil.JUnitMessageGenerator;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.sekarre.helpcenterchat.factories.ChatMockFactory.getChatWithDefaultUserMock;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChatMapperTest {

    private final ChatMapper chatMapper = Mappers.getMapper(ChatMapper.class);
    private JUnitMessageGenerator<?, ?> messageGenerator;

    @Test
    public void should_map_Chat_to_ChatInfoDTO() {
        messageGenerator = new JUnitMessageGenerator<>(Chat.class, ChatInfoDTO.class);

        //given
        final Chat from = getChatWithDefaultUserMock();

        //when
        ChatInfoDTO result = chatMapper.mapChatToChatInfoDTO(from);

        //then
        assertEquals(from.getId(), result.getId(),
                messageGenerator.getMessage("id", from.getId(), "id", result.getId()));
        assertEquals(from.getChannelName(), result.getChannelName(),
                messageGenerator.getMessage("channelName", from.getChannelName(), "channelName", result.getChannelName()));
        assertEquals(from.getChannelId(), result.getChannelId(),
                messageGenerator.getMessage("channelId", from.getChannelId(), "channelId", result.getChannelId()));
        assertEquals(from.getIssue().getId(), result.getIssueId(),
                messageGenerator.getMessage("issue.id", from.getIssue().getId(), "issueId", result.getIssueId()));
    }
}