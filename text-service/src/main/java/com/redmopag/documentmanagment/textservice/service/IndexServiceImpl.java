package com.redmopag.documentmanagment.textservice.service;

import com.redmopag.documentmanagment.common.*;
import com.redmopag.documentmanagment.textservice.exception.NotFoundException;
import com.redmopag.documentmanagment.textservice.model.DocumentText;
import com.redmopag.documentmanagment.textservice.repository.DocumentTextRepository;
import com.redmopag.documentmanagment.textservice.utils.mapper.DocumentTextMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<TextResponse> search(String text) {
        System.out.println("Поиск текста " + text);
        return documentTextRepository.findByTextContaining(text)
                .stream()
                .map(DocumentTextMapper.INSTANCE::toTextResponse)
                .toList();
    }

    @Override
    public TextResponse getDocumentTextById(String id) {
        var docText = documentTextRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Текст с id: " + id + " не найден"));
        return DocumentTextMapper.INSTANCE.toTextResponse(docText);
    }

    @Override
    public void deleteText(DeletedFile file) {
        var docId = file.getDocumentId();
        documentTextRepository.deleteAllByDocumentId(docId);
        System.out.println("Тексты с id " + docId + " удалены");
    }
}
