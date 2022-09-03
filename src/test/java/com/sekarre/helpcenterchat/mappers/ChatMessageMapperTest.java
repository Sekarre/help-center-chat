package com.sekarre.helpcenterchat.mappers;

import com.sekarre.helpcenterchat.DTO.ChatInfoDTO;
import com.sekarre.helpcenterchat.DTO.ChatMessageDTO;
import com.sekarre.helpcenterchat.domain.Chat;
import com.sekarre.helpcenterchat.domain.ChatMessage;
import com.sekarre.helpcenterchat.testutil.JUnitMessageGenerator;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.sekarre.helpcenterchat.factories.ChatMessageMockFactory.getChatMessageDTOMock;
import static com.sekarre.helpcenterchat.factories.ChatMessageMockFactory.getChatMessageMock;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChatMessageMapperTest {

    private final ChatMessageMapper chatMessageMapper = Mappers.getMapper(ChatMessageMapper.class);
    private JUnitMessageGenerator<?, ?> messageGenerator;

    @Test
    public void should_map_basic_fields_from_ChatMessage_to_ChatMessageDTO() {
        messageGenerator = new JUnitMessageGenerator<>(ChatInfoDTO.class, Chat.class);

        //given
        final ChatMessage from = getChatMessageMock();

        //when
        ChatMessageDTO result = chatMessageMapper.mapChatMessageToChatMessageDTO(from);

        //then
        assertEquals(from.getMessage(), result.getMessage(),
                messageGenerator.getMessage("message", from.getMessage(), "message", result.getMessage()));
        assertEquals(from.getFile(), result.getFile(),
                messageGenerator.getMessage("file", from.getFile(), "file", result.getFile()));
        assertEquals(from.getCreatedDateTime(), result.getCreatedDateTime(),
                messageGenerator.getMessage("createdDateTime", from.getCreatedDateTime(), "createdDateTime", result.getCreatedDateTime()));
    }

    @Test
    public void should_map_sender_fields_from_ChatMessage_to_ChatMessageDTO() {
        messageGenerator = new JUnitMessageGenerator<>(ChatMessage.class, ChatMessageDTO.class);

        //given
        final ChatMessage from = getChatMessageMock();

        //when
        ChatMessageDTO result = chatMessageMapper.mapChatMessageToChatMessageDTO(from);

        //then
        assertEquals(from.getSender().getId(), result.getSenderId(),
                messageGenerator.getMessage("sender.id", from.getSender().getId(), "senderId", result.getSenderId()));
        assertEquals(from.getSender().getFirstName(), result.getSenderName(),
                messageGenerator.getMessage("sender.firstName", from.getSender().getFirstName(), "senderName", result.getSenderName()));
        assertEquals(from.getSender().getLastName(), result.getSenderLastname(),
                messageGenerator.getMessage("sender.lastName", from.getSender().getLastName(), "senderLastname", result.getSenderLastname()));
    }

    @Test
    public void should_map_ChatMessageDTO_to_ChatMessage() {
        messageGenerator = new JUnitMessageGenerator<>(ChatMessageDTO.class, ChatMessage.class);

        //given
        final ChatMessageDTO from = getChatMessageDTOMock();

        //when
        ChatMessage result = chatMessageMapper.mapChatMessageDTOToMessage(from);

        //then
        assertEquals(from.getMessage(), result.getMessage(),
                messageGenerator.getMessage("message", from.getMessage(), "message", result.getMessage()));
        assertEquals(from.getFile(), result.getFile(),
                messageGenerator.getMessage("file", from.getFile(), "file", result.getFile()));
        assertEquals(from.getCreatedDateTime(), result.getCreatedDateTime(),
                messageGenerator.getMessage("createdDateTime", from.getCreatedDateTime(), "createdDateTime", result.getCreatedDateTime()));
    }
}