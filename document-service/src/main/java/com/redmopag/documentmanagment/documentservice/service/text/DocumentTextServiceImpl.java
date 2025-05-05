package com.redmopag.documentmanagment.documentservice.service.text;

import com.redmopag.documentmanagment.documentservice.exception.TextNotFoundException;
import com.redmopag.documentmanagment.documentservice.model.DocumentText;
import com.redmopag.documentmanagment.documentservice.repository.DocumentTextRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTextServiceImpl implements DocumentTextService {
    private final DocumentTextRepository repository;

    public DocumentTextServiceImpl(DocumentTextRepository repository) {
        this.repository = repository;
    }

    @Async
    @Override
    public void saveText(Long documentId, String text, String hocrContent) {
        DocumentText document = new DocumentText(documentId, text, hocrContent);
        repository.save(document);
    }

    @Override
    public List<DocumentText> getTextsByContaining(String text) {
        return repository.findByTextContaining(text);
    }

    @Override
    public DocumentText getTextById(Long documentId) {
        return repository.findById(documentId)
                .orElseThrow(() -> new TextNotFoundException("Текст документа с id: " +
                        documentId + " не найден"));
    }
}
