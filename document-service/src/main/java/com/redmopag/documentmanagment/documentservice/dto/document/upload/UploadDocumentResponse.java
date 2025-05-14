package com.redmopag.documentmanagment.documentservice.dto.document.upload;

import com.redmopag.documentmanagment.documentservice.model.DocumentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadDocumentResponse {
    private String name;
    private String type;
    private DocumentStatus status;
    private LocalDateTime expirationDate;
    private LocalDateTime uploadedAt;
    private LocalDateTime updatedAt;
}
