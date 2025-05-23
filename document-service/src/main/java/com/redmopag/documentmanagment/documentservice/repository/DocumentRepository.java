package com.redmopag.documentmanagment.documentservice.repository;

import com.redmopag.documentmanagment.documentservice.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findDocumentByExpirationDate(LocalDate expirationDate);

    List<Document> findByNameContaining(String name);

    Optional<Document> findByName(String name);

    List<Document> findAllByNameContainingAndUserName(String text, String userName);
}
