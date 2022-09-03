package com.sekarre.helpcenterchat.services.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sekarre.helpcenterchat.DTO.notification.NotificationQueueDTO;
import com.sekarre.helpcenterchat.exceptions.notification.NotificationSendFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.sekarre.helpcenterchat.util.MessageBrokerJsonHeaders.TYPE_ID_HEADER;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationSender {

    private final RabbitTemplate rabbitTemplate;

    private final Queue notificationQueue;
    private final ObjectMapper objectMapper;

    private static final String TYPE_ID_HEADER_NAME = "notificationQueueDTO";

    public void send(NotificationQueueDTO notificationQueueDTO) {
        try {
            Message jsonMessage = getMessage(notificationQueueDTO);
            rabbitTemplate.send(notificationQueue.getName(), jsonMessage);
        } catch (JsonProcessingException e) {
            throw new NotificationSendFailedException("Notification send failure for notification: " + notificationQueueDTO.toString());
        }
    }

    private Message getMessage(NotificationQueueDTO notificationQueueDTO) throws JsonProcessingException {
        String jsonObject = objectMapper.writeValueAsString(notificationQueueDTO);
        Message jsonMessage = MessageBuilder.withBody(jsonObject.getBytes())
                .andProperties(MessagePropertiesBuilder.newInstance().setContentType(ContentType.APPLICATION_JSON.getMimeType())
                        .build()).build();
        jsonMessage.getMessageProperties().setHeader(TYPE_ID_HEADER, TYPE_ID_HEADER_NAME);
        return jsonMessage;
    }
}
