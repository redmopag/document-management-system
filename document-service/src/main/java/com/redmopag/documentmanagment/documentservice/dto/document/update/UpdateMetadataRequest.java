package com.redmopag.documentmanagment.documentservice.dto.document.update;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMetadataRequest {
    @Min(1)
    private Long id;

    @NotBlank
    private String name;

    @Nullable
    private String category;

    @Nullable
    private LocalDate expirationDate;
}
