package com.redmopag.documentmanagment.documentservice.service.notifier;

import java.time.LocalDate;

public interface NotifierService {
    void sendNotificationExpiration(String title, LocalDate expirationDate);
}
