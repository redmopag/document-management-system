package com.redmopag.documentmanagment.documentservice.service.document;

import com.redmopag.documentmanagment.common.*;
import com.redmopag.documentmanagment.documentservice.dto.document.*;
import com.redmopag.documentmanagment.documentservice.dto.document.update.*;
import com.redmopag.documentmanagment.documentservice.dto.document.upload.UploadDocumentRequest;
import com.redmopag.documentmanagment.documentservice.exception.badrequest.NotAllowedFileStatusException;
import com.redmopag.documentmanagment.documentservice.exception.notFound.DocumentNotFoundException;
import com.redmopag.documentmanagment.documentservice.kafka.producer.DeleteProducer;
import com.redmopag.documentmanagment.documentservice.model.*;
import com.redmopag.documentmanagment.documentservice.repository.DocumentRepository;
import com.redmopag.documentmanagment.documentservice.service.EmitterService;
import com.redmopag.documentmanagment.documentservice.service.storage.StorageService;
import com.redmopag.documentmanagment.documentservice.service.text.TextService;
import com.redmopag.documentmanagment.documentservice.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//TODO: выделить классы по возможности
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DeleteProducer deleteProducer;
    private final StorageService storageService;
    private final TextService textService;
    private final DocumentValidator documentValidator;
    private final EmitterService emitterService;

    @Override
    public DocumentSummaryResponse uploadDocument(UploadDocumentRequest uploadRequest) {
        var files = uploadRequest.getFiles();
        documentValidator.validateFiles(files);
        Optional<Document> foundDoc = documentRepository.findByName(uploadRequest.getFilename());
        if (foundDoc.isPresent()) {
            return DocumentMapper.INSTANCE.toDocumentSummaryResponse(foundDoc.get());
        } else {
            var savedDocument = saveDocument(uploadRequest, files);
            return DocumentMapper.INSTANCE.toDocumentSummaryResponse(savedDocument);
        }
    }

    private Document saveDocument(UploadDocumentRequest uploadRequest, List<MultipartFile> files) {
        var savedDocument = saveMetadata(uploadRequest);
        storageService.upload(savedDocument.getId(), files);
        System.out.println("Сохранён документ: " + savedDocument.getName() + " - " + savedDocument.getId());
        return savedDocument;
    }

    private Document saveMetadata(UploadDocumentRequest uploadRequest) {
        return documentRepository.save(Document.builder()
                .status(DocumentStatus.PROCESSING)
                .category(uploadRequest.getCategory())
                .expirationDate(uploadRequest.getExpirationDate())
                .userName(uploadRequest.getUsername())
                .name(uploadRequest.getFilename())
                .build());
    }

    @Override
    public List<Document> findExpiringAt(LocalDate expirationDate) {
        return documentRepository.findDocumentByExpirationDate(expirationDate);
    }

    @Override
    public List<DocumentSummaryResponse> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(DocumentMapper.INSTANCE::toDocumentSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentDetailsResponse getDocumentDetails(Long id) {
        Document doc = getDocumentById(id);
        if (!doc.getStatus().equals(DocumentStatus.CONFIRMED)) {
            throw new NotAllowedFileStatusException("Документ " + doc.getName() + " ещё не обработан");
        }
        TextResponse docText = textService.getTextById(doc.getTextId());
        String originalDocUrl = storageService.getDownloadUrl(doc.getObjectKey());
        return buildDocumentDetailsResponse(doc, originalDocUrl, docText.getText());
    }

    private DocumentDetailsResponse buildDocumentDetailsResponse(
            Document doc, String originalDocUrl, String hocrContent) {
        var details = DocumentMapper.INSTANCE.toDocumentDetailsResponse(doc);
        details.setText(hocrContent);
        details.setDownloadUrl(originalDocUrl);
        return details;
    }

    // TODO: вынести фильтрацию по имени в text-service
    @Override
    public List<DocumentSummaryResponse> findDocumentByText(String username, String text) {
        var docs = documentRepository.findAllByNameContainingAndUserName(text, username);
        var textContainingDocs = findDocumentsByTextAndUserName(text, username);
        docs.addAll(textContainingDocs);
        return docs.stream()
                .distinct()
                .map(DocumentMapper.INSTANCE::toDocumentSummaryResponse)
                .collect(Collectors.toList());
    }

    private Collection<Document> findDocumentsByTextAndUserName(String text, String userName) {
        List<TextResponse> docText = foundText(text);
        return getFilteredByUsernameDocuments(userName, docText);
    }

    private List<TextResponse> foundText(String text) {
        List<TextResponse> docText = textService.getTextByContaining(text);
        if (docText.isEmpty()) {
            return Collections.emptyList();
        }
        return docText;
    }

    private List<Document> getFilteredByUsernameDocuments(String userName, List<TextResponse> docText) {
        Iterable<Long> ids = docText.stream()
                .map(TextResponse::getDocumentId)
                .collect(Collectors.toList());
        return documentRepository.findAllById(ids)
                .stream()
                .filter(doc -> doc.getUserName().equals(userName))
                .toList();
    }

    @Transactional
    @Override
    public void updateDocumentMetadata(MetadataEvent event) {
        var document = updateDocument(event);
        documentRepository.save(document);
        System.out.println("Метаданные документа " + document.getName() +
                " - " + document.getId() + " обновлены");
        notifyStatusChange(document.getId(), document.getStatus());
    }

    private Document updateDocument(MetadataEvent event) {
        Document document = getDocumentById(event.getDocumentId());
        document.setUpdatedAt(LocalDateTime.now());
        document.setObjectKey(event.getObjectKey());
        document.setTextId(event.getTextId());
        document.setStatus(DocumentStatus.CONFIRMED);
        return document;
    }

    private Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }

    //TODO: переделать на флаг удаления и окончательное удаление после подтверждения от других сервисов
    @Override
    public void deleteDocument(Long id) {
        try {
            var doc = getDocumentById(id);
            if (!doc.getStatus().equals(DocumentStatus.CONFIRMED)) {
                throw new NotAllowedFileStatusException("Файл " + doc.getName() + " ещё не обработан");
            }
            var deletedFile = new DeletedFile(doc.getId(), doc.getObjectKey());
            deleteProducer.fileDeleted(deletedFile);
            documentRepository.deleteById(id);
        } catch (DocumentNotFoundException ignored) {
        }
    }

    @Transactional
    @Override
    public void updateDocument(UpdateMetadataRequest request) {
        var doc = getDocumentById(request.getId());
        if (request.getName() != null) {
            doc.setName(request.getName());
        }
        if (request.getCategory() != null) {
            doc.setCategory(request.getCategory());
        }
        if (request.getExpirationDate() != null) {
            doc.setExpirationDate(request.getExpirationDate());
        }
        documentRepository.save(doc);
    }

    @Override
    public SseEmitter registerEmitter() {
        return emitterService.registerEmitter();
    }

    private void notifyStatusChange(Long documentId, DocumentStatus status) {
        StatusChanged statusChanged = new StatusChanged(documentId, status);
        emitterService.notifyEmitter(statusChanged, "status-update");
    }
}
