package com.sekarre.helpcenterchat.DTO.notification;

import com.sekarre.helpcenterchat.domain.enums.EventType;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class NotificationQueueDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2650647051596909952L;

    private Long id;
    private Long userId;
    private String message;
    private String destinationId;
    private String eventType;
    private String createdAt;

    public NotificationQueueDTO(String destinationId, Long userId, EventType eventType, String createdAt) {
        this.eventType = eventType.name();
        this.destinationId = destinationId;
        this.createdAt = createdAt;
        this.userId = userId;
    }
}
