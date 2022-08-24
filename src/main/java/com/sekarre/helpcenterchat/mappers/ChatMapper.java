package com.sekarre.helpcenterchat.mappers;

import com.sekarre.helpcenterchat.DTO.ChatInfoDTO;
import com.sekarre.helpcenterchat.domain.Chat;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class ChatMapper {

    @Mapping(target = "issueId", source = "chat.issue.id")
    public abstract ChatInfoDTO mapChatToChatInfoDTO(Chat chat);
}
