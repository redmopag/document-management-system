package com.redmopag.documentmanagment.documentservice.service.notifier;

import com.redmopag.documentmanagment.documentservice.dto.DocumentNotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NotifierServiceImpl implements NotifierService {
    private final KafkaTemplate<String, DocumentNotificationEvent> kafkaProducer;

    public NotifierServiceImpl(KafkaTemplate<String, DocumentNotificationEvent> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void sendNotificationExpiration(String title, LocalDate expirationDate) {
        DocumentNotificationEvent notificationEvent = new DocumentNotificationEvent(title, expirationDate);
        kafkaProducer.send("document-expiration-topic", notificationEvent);
    }
}
