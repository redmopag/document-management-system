package com.redmopag.documentmanagment.filestorageservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OcrFileEvent {
    private Long fileId;
    private String objectKey;
    private String downloadUrl;
}
