package com.redmopag.documentmanagment.filestorageservice.kafka.consumer;

import com.redmopag.documentmanagment.common.DeletedFile;
import com.redmopag.documentmanagment.filestorageservice.service.storage.StorageService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DeletedFileConsumer {
    private final StorageService storageService;

    public DeletedFileConsumer(StorageService storageService) {
        this.storageService = storageService;
    }

    @KafkaListener(topics = "deleted-file")
    public void fileDeleted(DeletedFile deletedFile) {
        storageService.deleteFile(deletedFile);
    }
}
