package com.redmopag.documentmanagment.filestorageservice.kafka.producer;

import com.redmopag.documentmanagment.common.OcrFileEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.*;

@Service
public class OcrKafkaProducer {
    private final KafkaTemplate<String, OcrFileEvent> kafkaTemplate;

    public OcrKafkaProducer(KafkaTemplate<String, OcrFileEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void ocrFile(Long fileId, String objectKey, String downloadUrl){
        OcrFileEvent ocrFileEvent = new OcrFileEvent(fileId, objectKey, downloadUrl);
        kafkaTemplate.send("ocr-file", ocrFileEvent);
    }
}
