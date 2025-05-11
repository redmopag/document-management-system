package com.redmopag.documentmanagment.documentservice.utils;

import com.redmopag.documentmanagment.documentservice.dto.document.*;
import com.redmopag.documentmanagment.documentservice.model.Document;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocumentMapper {
    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    DocumentSummaryResponse toDocumentSummaryResponse(Document document);

    DocumentDetailsResponse toDocumentDetailsResponse(Document document);
}
