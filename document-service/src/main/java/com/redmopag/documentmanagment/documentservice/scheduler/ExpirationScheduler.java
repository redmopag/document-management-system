package com.redmopag.documentmanagment.documentservice.scheduler;

import com.redmopag.documentmanagment.documentservice.kafka.producer.NotificationProducer;
import com.redmopag.documentmanagment.documentservice.model.Document;
import com.redmopag.documentmanagment.documentservice.service.document.DocumentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ExpirationScheduler {
    private final DocumentService documentService;
    private final NotificationProducer notificationProducer;

    public ExpirationScheduler(DocumentService documentService, NotificationProducer notificationProducer) {
        this.documentService = documentService;
        this.notificationProducer = notificationProducer;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void checkAndNotifyExpiration() {
        List<Document> expiringDocs = documentService.findExpiringAt(LocalDate.now().plusDays(7));
        if (expiringDocs == null) return;
        for (var expiringDoc : expiringDocs) {
            notificationProducer.sendNotificationExpiration(expiringDoc.getName(), expiringDoc.getExpirationDate());
        }
    }
}
