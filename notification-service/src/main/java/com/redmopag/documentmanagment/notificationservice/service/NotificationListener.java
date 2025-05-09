package com.redmopag.documentmanagment.notificationservice.service;

import com.redmopag.documentmanagment.common.DocumentNotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {
    @KafkaListener(topics = "document-expiration-topic", groupId = "notification-group")
    public void listen(DocumentNotificationEvent event) {
        System.out.println("Уведомление о окончании документе с истекающим сроком действия: " +
                event.getTitle());
    }
}
