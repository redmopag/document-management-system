package com.redmopag.documentmanagment.documentservice.utils;

import com.redmopag.documentmanagment.documentservice.dto.UploadDocumentResponse;
import com.redmopag.documentmanagment.documentservice.model.Document;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocumentMapper {
    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    UploadDocumentResponse toUploadDocumentResponse(Document document);
}
