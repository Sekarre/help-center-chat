package com.sekarre.helpcenterchat.services.notification;

import com.sekarre.helpcenterchat.DTO.notification.NotificationLimiterQueueDTO;
import com.sekarre.helpcenterchat.DTO.notification.NotificationQueueDTO;
import com.sekarre.helpcenterchat.domain.enums.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.sekarre.helpcenterchat.config.WebSocketConfigRabbitMQ.ERRORS;
import static com.sekarre.helpcenterchat.util.DateUtil.getCurrentDateTimeFormatted;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {


    private final NotificationSender notificationSender;
    private final NotificationLimiterQueueSender notificationLimiterQueueSender;

    @Override
    public void startNotificationForDestination(String destinationId, Long userId, EventType eventType) {
        if (!ERRORS.equals(destinationId)) {
            notificationLimiterQueueSender.send(new NotificationLimiterQueueDTO(destinationId, eventType.name(), userId, true));
        }
    }

    @Override
    public void stopNotificationForDestination(String destinationId, Long userId, EventType eventType) {
        if (!ERRORS.equals(destinationId)) {
            notificationLimiterQueueSender.send(new NotificationLimiterQueueDTO(destinationId, eventType.name(), userId, false));
        }
    }

    @Override
    public void sendNotification(String destinationId, Long userId, EventType eventType) {
        notificationSender.send(new NotificationQueueDTO(destinationId, userId, eventType, getCurrentDateTimeFormatted()));
    }
}
