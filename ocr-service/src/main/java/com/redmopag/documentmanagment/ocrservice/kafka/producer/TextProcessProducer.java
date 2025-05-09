package com.redmopag.documentmanagment.ocrservice.kafka.producer;

import com.redmopag.documentmanagment.common.ProcessTextEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TextProcessProducer {
    private final KafkaTemplate<String, ProcessTextEvent> kafkaTemplate;

    public TextProcessProducer(KafkaTemplate<String, ProcessTextEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void processText(ProcessTextEvent processTextEvent) {
        kafkaTemplate.send("process-text", processTextEvent);
    }
}
