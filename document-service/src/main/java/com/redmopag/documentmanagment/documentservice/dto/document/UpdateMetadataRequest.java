package com.redmopag.documentmanagment.documentservice.dto.document;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMetadataRequest {
    private Long id;
    private String name;
    private String category;
    private LocalDate expirationDate;
}
