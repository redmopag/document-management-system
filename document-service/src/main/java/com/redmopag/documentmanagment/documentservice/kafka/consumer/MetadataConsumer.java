package com.redmopag.documentmanagment.documentservice.kafka.consumer;

import com.redmopag.documentmanagment.common.MetadataEvent;
import com.redmopag.documentmanagment.documentservice.service.document.DocumentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MetadataConsumer {
    private final KafkaTemplate<String, MetadataEvent> kafkaTemplate;
    private final DocumentService documentService;

    public MetadataConsumer(KafkaTemplate<String, MetadataEvent> kafkaTemplate, DocumentService documentService) {
        this.kafkaTemplate = kafkaTemplate;
        this.documentService = documentService;
    }

    @KafkaListener(topics = "save-metadata-topic", groupId = "metadata-group")
    public void receiveMetadata(MetadataEvent metadataEvent) {
        documentService.updateDocumentMetadata(metadataEvent);
    }
}
