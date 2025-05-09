package com.redmopag.documentmanagment.documentservice.kafka.producer;

import com.redmopag.documentmanagment.common.DocumentNotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NotificationProducer {
    private final KafkaTemplate<String, DocumentNotificationEvent> kafkaProducer;


    public NotificationProducer(KafkaTemplate<String, DocumentNotificationEvent> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void sendNotificationExpiration(String title, LocalDate expirationDate) {
        DocumentNotificationEvent notificationEvent = new DocumentNotificationEvent(title, expirationDate);
        kafkaProducer.send("document-expiration-topic", notificationEvent);
    }
}
