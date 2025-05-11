package com.redmopag.documentmanagment.textservice.kafka.consumer;

import com.redmopag.documentmanagment.common.DeletedFile;
import com.redmopag.documentmanagment.textservice.service.IndexService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DeletedFileConsumer {
    private final IndexService indexService;

    public DeletedFileConsumer(IndexService indexService) {
        this.indexService = indexService;
    }

    @KafkaListener(topics = "deleted-file")
    public void fileDeleted(DeletedFile deletedFile) {
        indexService.deleteText(deletedFile);
    }
}
