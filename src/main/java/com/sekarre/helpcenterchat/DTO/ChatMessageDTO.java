package com.sekarre.helpcenterchat.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sekarre.helpcenterchat.util.DateUtil;
import com.sekarre.helpcenterchat.validators.AtLeastOneFieldNotEmpty;
import com.sekarre.helpcenterchat.validators.Base64Encoded;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@AtLeastOneFieldNotEmpty(fields = {"message", "file"})
public class ChatMessageDTO {

    @Length(max = 254)
    private String message;

    @Base64Encoded(nullAllowed = true)
    private String file;

    private Long senderId;

    private String senderName;
    private String senderLastname;

    @JsonFormat(pattern= DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime createdDateTime;
}
