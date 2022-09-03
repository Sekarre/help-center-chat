package com.sekarre.helpcenterchat.services.notification;

import com.sekarre.helpcenterchat.domain.enums.EventType;

public interface NotificationService {

    void startNotificationForDestination(String destinationId, Long userId, EventType eventType);

    void stopNotificationForDestination(String destinationId, Long userId, EventType eventType);

    void sendNotification(String destinationId, Long userId, EventType eventType);
}
