package com.sekarre.helpcenterchat.services.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sekarre.helpcenterchat.DTO.notification.NotificationQueueDTO;
import com.sekarre.helpcenterchat.exceptions.notification.NotificationSendFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationSender {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;
    private final ObjectMapper objectMapper;

    public void sendNotification(NotificationQueueDTO notificationQueueDTO) {
        try {
            Message jsonMessage = getMessage(notificationQueueDTO);
            rabbitTemplate.send(queue.getName(), jsonMessage);
        } catch (JsonProcessingException e) {
            throw new NotificationSendFailedException("Notification send failure for notification: " + notificationQueueDTO.toString());
        }
    }

    private Message getMessage(NotificationQueueDTO notificationQueueDTO) throws JsonProcessingException {
        String jsonObject = objectMapper.writeValueAsString(notificationQueueDTO);
        Message jsonMessage = MessageBuilder.withBody(jsonObject.getBytes())
                .andProperties(MessagePropertiesBuilder.newInstance().setContentType("application/json")
                        .build()).build();
        jsonMessage.getMessageProperties().setHeader("__TypeId__", "notificationQueueDTO");
        return jsonMessage;
    }
}