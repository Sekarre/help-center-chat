package com.sekarre.helpcenterchat.listeners;

import com.sekarre.helpcenterchat.domain.User;
import com.sekarre.helpcenterchat.exceptions.handler.EventListenerErrorHandling;
import com.sekarre.helpcenterchat.factories.ChatMessageBotFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sekarre.helpcenterchat.util.SimpMessageHeaderUtil.getUserFromHeaders;


@RequiredArgsConstructor
@Slf4j
@Component
public class ChatListener {

    private final Map<String, String> destinationTracker = new HashMap<>();
    private final SimpMessagingTemplate simpMessagingTemplate;
//    private final EventNotificationService eventNotificationService;

    @EventListenerErrorHandling
    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        final String destination = destinationTracker.get(headers.getSessionId());
        User user = getUserFromHeaders(headers);
        if (Objects.isNull(destination)) {
            return;
        }
        destinationTracker.remove(headers.getSessionId());
//        eventNotificationService.startNotificationForDestination(
//                getChannelIdFromDestinationHeader(destination), user.getId(), EventType.NEW_CHAT_MESSAGE);
        simpMessagingTemplate.convertAndSend(destination,
                ChatMessageBotFactory.getGoodbyeChatMessage(user.getFirstName() + " " + user.getLastName()));
    }

    @EventListenerErrorHandling
    @EventListener
    public void onSubscribeEvent(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        User user = getUserFromHeaders(headers);
        final String destination = headers.getDestination();
        if (Objects.isNull(destination)) {
            return;
        }
        destinationTracker.put(headers.getSessionId(), destination);
//        eventNotificationService.stopNotificationForDestination(
//                getChannelIdFromDestinationHeader(destination), user.getId(), EventType.NEW_CHAT_MESSAGE);
        simpMessagingTemplate.convertAndSend(destination,
                ChatMessageBotFactory.getWelcomeChatMessage(user.getFirstName() + " " + user.getLastName()));
    }
}
