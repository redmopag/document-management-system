package com.redmopag.documentmanagment.documentservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrResponse {
    private String textId;
    private String suggestedType;
    private LocalDateTime suggestedExpirationDate;
}
