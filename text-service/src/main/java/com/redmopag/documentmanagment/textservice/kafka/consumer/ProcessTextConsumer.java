package com.redmopag.documentmanagment.textservice.kafka.consumer;

import com.redmopag.documentmanagment.common.*;
import com.redmopag.documentmanagment.textservice.kafka.producer.SaveMetadataProducer;
import com.redmopag.documentmanagment.textservice.service.IndexService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProcessTextConsumer {
    private final IndexService indexService;
    private final SaveMetadataProducer saveMetadataProducer;

    public ProcessTextConsumer(IndexService indexService, SaveMetadataProducer saveMetadataProducer) {
        this.indexService = indexService;
        this.saveMetadataProducer = saveMetadataProducer;
    }

    @KafkaListener(topics = "process-text", groupId = "process-text-group")
    public void processText(ProcessTextEvent processTextEvent) {
        MetadataEvent metadataEvent = indexService.processText(processTextEvent);
        saveMetadataProducer.sendMetadata(metadataEvent);
    }
}
