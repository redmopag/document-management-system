package com.redmopag.documentmanagment.common;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrFileEvent {
    private Long fileId;
    private String objectKey;
    private String downloadUrl;
}
