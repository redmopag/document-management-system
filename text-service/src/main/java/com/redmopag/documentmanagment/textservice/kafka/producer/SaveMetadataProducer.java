package com.redmopag.documentmanagment.textservice.kafka.producer;

import com.redmopag.documentmanagment.common.MetadataEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SaveMetadataProducer {
    private final KafkaTemplate<String, MetadataEvent> kafkaTemplate;

    public SaveMetadataProducer(KafkaTemplate<String, MetadataEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMetadata(MetadataEvent metadataEvent) {
        kafkaTemplate.send("save-metadata-topic", metadataEvent);
    }
}
