package com.redmopag.documentmanagment.documentservice.dto;

import lombok.*;

import java.time.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrResponse {
    private String text;
    private String hocrContent;
    private String category;
    private LocalDate expirationDate;
    private List<LocalDate> expirationDates;}
