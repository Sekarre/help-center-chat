package com.sekarre.helpcenterchat.exceptions.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sekarre.helpcenterchat.util.DateUtil;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessage {

    private String cause;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime timestamp;
}
