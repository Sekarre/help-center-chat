package com.sekarre.helpcenterchat.services.impl;

import com.sekarre.helpcenterchat.DTO.notification.NotificationLimiterQueueDTO;
import com.sekarre.helpcenterchat.DTO.notification.NotificationQueueDTO;
import com.sekarre.helpcenterchat.domain.enums.EventType;
import com.sekarre.helpcenterchat.services.NotificationService;
import com.sekarre.helpcenterchat.services.notification.NotificationLimiterQueueSender;
import com.sekarre.helpcenterchat.services.notification.NotificationQueueSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.sekarre.helpcenterchat.util.DateUtil.getCurrentDateTimeFormatted;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {


    private final NotificationQueueSender notificationQueueSender;
    private final NotificationLimiterQueueSender notificationLimiterQueueSender;

    @Override
    public void startNotificationForDestination(String destinationId, Long userId, EventType eventType) {
        notificationLimiterQueueSender.send(getNotificationLimiterQueueDTO(destinationId, userId, eventType, true));
    }

    @Override
    public void stopNotificationForDestination(String destinationId, Long userId, EventType eventType) {
        notificationLimiterQueueSender.send(getNotificationLimiterQueueDTO(destinationId, userId, eventType, false));
    }

    @Override
    public void sendNotification(String destinationId, Long userId, EventType eventType) {
        notificationQueueSender.send(getNotificationQueueDTO(destinationId, userId, eventType));
    }

    private NotificationQueueDTO getNotificationQueueDTO(String destinationId, Long userId, EventType eventType) {
        return NotificationQueueDTO.builder()
                .eventType(eventType.name())
                .destinationId(destinationId)
                .createdAt(getCurrentDateTimeFormatted())
                .userId(userId)
                .build();
    }

    private NotificationLimiterQueueDTO getNotificationLimiterQueueDTO(String destinationId, Long userId, EventType eventType, boolean isLimited) {
        return NotificationLimiterQueueDTO.builder()
                .eventType(eventType.name())
                .destinationId(destinationId)
                .userId(userId)
                .isLimited(isLimited)
                .build();
    }
}
