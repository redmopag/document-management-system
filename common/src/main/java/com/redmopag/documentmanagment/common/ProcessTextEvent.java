package com.redmopag.documentmanagment.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessTextEvent {
    private Long fileId;
    private String text;
    private String hocrContent;
    private String objectKey;
}
