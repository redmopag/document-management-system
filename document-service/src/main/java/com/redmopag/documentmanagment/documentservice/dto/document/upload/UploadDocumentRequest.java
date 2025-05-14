package com.redmopag.documentmanagment.documentservice.dto.document.upload;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadDocumentRequest {
    @NotEmpty(message = "Файл должен присутствовать")
    private List<MultipartFile> files;

    @Nullable
    private String category;

    @Nullable
    private LocalDate expirationDate;

    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String username;

    @NotBlank(message = "Имя файла не должно быть пустым")
    private String filename;
}