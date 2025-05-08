package com.redmopag.documentmanagment.filestorageservice.client.kafka;

import com.redmopag.documentmanagment.filestorageservice.dto.OcrFileEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OcrKafkaClient {
    private final KafkaTemplate<String, OcrFileEvent> kafkaTemplate;

    public OcrKafkaClient(KafkaTemplate<String, OcrFileEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void ocrFile(Long fileId, String objectKey, String downloadUrl){
        OcrFileEvent ocrFileEvent = new OcrFileEvent(fileId, objectKey, downloadUrl);
        kafkaTemplate.send("ocr-file", ocrFileEvent);
    }
}
