package com.redmopag.documentmanagment.ocrservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrRequest {
    private String filename;
    private String contentType;
    private byte[] fileContent;
}
