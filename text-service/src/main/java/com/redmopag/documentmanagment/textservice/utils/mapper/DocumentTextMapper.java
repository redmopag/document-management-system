package com.redmopag.documentmanagment.textservice.utils.mapper;

import com.redmopag.documentmanagment.common.ProcessTextEvent;
import com.redmopag.documentmanagment.textservice.model.DocumentText;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocumentTextMapper {
    DocumentTextMapper INSTANCE = Mappers.getMapper(DocumentTextMapper.class);

    @Mapping(source = "fileId", target = "documentId")
    DocumentText toDocumentText(ProcessTextEvent processTextEvent);
}
