package com.redmopag.documentmanagment.ocrservice.kafka.consumer;

import com.redmopag.documentmanagment.ocrservice.dto.*;
import com.redmopag.documentmanagment.ocrservice.kafka.producer.TextProcessProducer;
import com.redmopag.documentmanagment.ocrservice.service.OcrService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OcrFileConsumer {
    private final OcrService ocrService;
    private final TextProcessProducer textProcessProducer;

    public OcrFileConsumer(OcrService ocrService, TextProcessProducer textProcessProducer) {
        this.ocrService = ocrService;
        this.textProcessProducer = textProcessProducer;
    }

    @KafkaListener(topics = "ocr-file", groupId = "ocr-group")
    public void ocrFile(OcrFileEvent ocrFileEvent) {
        var recognizedText = ocrService.recognize(ocrFileEvent);
        textProcessProducer.processText(recognizedText);
    }
}
