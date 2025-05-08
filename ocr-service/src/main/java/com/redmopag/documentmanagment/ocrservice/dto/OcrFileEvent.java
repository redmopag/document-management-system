package com.redmopag.documentmanagment.ocrservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OcrFileEvent {
    private Long fileId;
    private String objectKey;
    private String downloadUrl;
}

