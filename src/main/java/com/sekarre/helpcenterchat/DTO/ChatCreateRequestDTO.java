package com.sekarre.helpcenterchat.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChatCreateRequestDTO {

    private String channelName;
    private Long[] usersId;
}