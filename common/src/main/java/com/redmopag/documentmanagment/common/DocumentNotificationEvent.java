package com.redmopag.documentmanagment.common;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentNotificationEvent {
    private String title;
    private LocalDate expirationDate;
}
