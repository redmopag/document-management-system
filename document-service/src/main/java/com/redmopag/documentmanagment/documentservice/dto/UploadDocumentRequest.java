package com.redmopag.documentmanagment.documentservice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadDocumentRequest {
    private MultipartFile file;
}
