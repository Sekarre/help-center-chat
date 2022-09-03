package com.sekarre.helpcenterchat.services.notification;

import com.sekarre.helpcenterchat.DTO.notification.NotificationLimiterQueueDTO;
import com.sekarre.helpcenterchat.DTO.notification.NotificationQueueDTO;
import com.sekarre.helpcenterchat.domain.enums.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NotificationServiceImplTest {

    @Mock
    private NotificationSender notificationSender;

    @Mock
    private NotificationLimiterQueueSender notificationLimiterQueueSender;

    private NotificationService notificationService;

    private static final String destinationId = "destinationId";
    private static final Long userId = 1L;
    private static final EventType eventType = EventType.CHAT;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationService = new NotificationServiceImpl(notificationSender, notificationLimiterQueueSender);
    }

    @Test
    void should_start_notification_for_destination_by_sending_proper_object() {
        //given
        NotificationLimiterQueueDTO notificationLimiterQueueDTO = NotificationLimiterQueueDTO.builder()
                .isLimited(true)
                .userId(userId)
                .destinationId(destinationId)
                .eventType(eventType.name())
                .build();

        //when
        notificationService.startNotificationForDestination(destinationId, userId, eventType);

        //then
        verify(notificationLimiterQueueSender, times(1)).send(notificationLimiterQueueDTO);
    }

    @Test
    void should_stop_notification_for_destination_by_sending_proper_object() {
        //given
        NotificationLimiterQueueDTO notificationLimiterQueueDTO = NotificationLimiterQueueDTO.builder()
                .isLimited(false)
                .userId(userId)
                .destinationId(destinationId)
                .eventType(eventType.name())
                .build();

        //when
        notificationService.stopNotificationForDestination(destinationId, userId, eventType);

        //then
        verify(notificationLimiterQueueSender, times(1)).send(notificationLimiterQueueDTO);
    }

    @Test
    void should_send_notification() {
        //when
        notificationService.sendNotification(destinationId, userId, eventType);

        //then
        verify(notificationSender, times(1)).send(any(NotificationQueueDTO.class));
    }
}