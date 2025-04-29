package com.redmopag.documentmanagment.ocrservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OcrResponse {
    private String text;
    private String hocrContent;
    private String category;
    private LocalDate expirationDate;
    private List<LocalDate> expirationDates;
}
