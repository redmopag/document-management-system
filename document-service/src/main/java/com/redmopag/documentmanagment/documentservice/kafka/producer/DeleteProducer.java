package com.redmopag.documentmanagment.documentservice.kafka.producer;

import com.redmopag.documentmanagment.common.DeletedFile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DeleteProducer {
    public final KafkaTemplate<String, DeletedFile> kafkaTemplate;

    public DeleteProducer(KafkaTemplate<String, DeletedFile> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void fileDeleted(DeletedFile deletedFile) {
        kafkaTemplate.send("deleted-file", deletedFile);
    }
}
