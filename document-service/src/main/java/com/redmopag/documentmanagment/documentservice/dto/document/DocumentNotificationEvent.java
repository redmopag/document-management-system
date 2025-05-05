package com.redmopag.documentmanagment.documentservice.dto.document;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentNotificationEvent {
    private String title;
    private LocalDate expirationDate;
}
