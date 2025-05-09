package com.redmopag.documentmanagment.common;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataEvent {
    private Long documentId;
    private String textId;
    private String objectKey;
}
