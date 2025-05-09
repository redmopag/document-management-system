package com.redmopag.documentmanagment.textservice.service;

import com.redmopag.documentmanagment.common.*;
import com.redmopag.documentmanagment.textservice.model.DocumentText;
import com.redmopag.documentmanagment.textservice.repository.DocumentTextRepository;
import com.redmopag.documentmanagment.textservice.utils.mapper.DocumentTextMapper;
import org.springframework.stereotype.Service;

@Service
public class IndexServiceImpl implements IndexService {
    private final DocumentTextRepository documentTextRepository;

    public IndexServiceImpl(DocumentTextRepository documentTextRepository) {
        this.documentTextRepository = documentTextRepository;
    }

    @Override
    public MetadataEvent processText(ProcessTextEvent processTextEvent) {
        var documentText = DocumentTextMapper.INSTANCE.toDocumentText(processTextEvent);
        var savedDocument = documentTextRepository.save(documentText);
        System.out.println("Текст документа " + processTextEvent.getFileId() +
                " сохранён с id: " + savedDocument.getId());
        return buildMetadataResponse(processTextEvent, savedDocument);
    }

    private MetadataEvent buildMetadataResponse(ProcessTextEvent processTextEvent,
                                                DocumentText savedDocument) {
        return new MetadataEvent(processTextEvent.getFileId(),
                savedDocument.getId(),
                processTextEvent.getObjectKey());
    }
}
