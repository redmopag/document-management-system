package com.redmopag.documentmanagment.notificationservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DocumentNotificationEvent {
    private String title;
    private LocalDate expirationDate;
}
