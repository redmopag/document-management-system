package com.redmopag.documentmanagment.documentservice.dto.document;

import com.redmopag.documentmanagment.documentservice.model.DocumentStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusChanged {
    private Long documentId;
    private DocumentStatus status;
}
