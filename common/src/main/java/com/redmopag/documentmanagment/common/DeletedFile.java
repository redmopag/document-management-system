package com.redmopag.documentmanagment.common;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeletedFile {
    private Long documentId;
    private String objectKey;
}
