package com.redmopag.documentmanagment.documentservice.repository;

import com.redmopag.documentmanagment.documentservice.model.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
    List<Document> findDocumentByExpirationDate(LocalDate expirationDate);
}
