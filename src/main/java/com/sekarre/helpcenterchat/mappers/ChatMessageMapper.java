package com.sekarre.helpcenterchat.mappers;

import com.sekarre.helpcenterchat.DTO.ChatMessageDTO;
import com.sekarre.helpcenterchat.domain.ChatMessage;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class ChatMessageMapper {

    @Mapping(target = "senderId", source = "chatMessage.sender.id")
    @Mapping(target = "senderName", source = "chatMessage.sender.firstName")
    @Mapping(target = "senderLastname", source = "chatMessage.sender.lastName")
    public abstract ChatMessageDTO mapChatMessageToChatMessageDTO(ChatMessage chatMessage);

    public abstract ChatMessage mapChatMessageDTOToMessage(ChatMessageDTO chatMessageDTO);
}
