package com.sekarre.helpcenterchat.mappers;

import com.sekarre.helpcenterchat.DTO.ChatInfoDTO;
import com.sekarre.helpcenterchat.domain.Chat;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class ChatMapper {

    public abstract ChatInfoDTO mapChatToChatInfoDTO(Chat chat);
}
