package com.redmopag.documentmanagment.documentservice.scheduler;

import com.redmopag.documentmanagment.documentservice.model.Document;
import com.redmopag.documentmanagment.documentservice.service.document.DocumentService;
import com.redmopag.documentmanagment.documentservice.service.notifier.NotifierService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ExpirationScheduler {
    private final NotifierService notifierService;
    private final DocumentService documentService;

    public ExpirationScheduler(NotifierService notifierService, DocumentService documentService) {
        this.notifierService = notifierService;
        this.documentService = documentService;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void checkAndNotifyExpiration() {
        List<Document> expiringDocs = documentService.findExpiringAt(LocalDate.now().plusDays(7));
        if (expiringDocs == null) return;
        for (var expiringDoc : expiringDocs) {
            notifierService.sendNotificationExpiration(expiringDoc.getName(), expiringDoc.getExpirationDate());
        }
    }
}
