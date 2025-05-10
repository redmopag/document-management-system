package com.redmopag.documentmanagment.common;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextResponse {
    private Long documentId;
    private String text;
    private String hocrContent;
}
