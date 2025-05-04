package com.redmopag.documentmanagment.documentservice.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentNotificationEvent {
    private String title;
    private LocalDate expirationDate;
}
