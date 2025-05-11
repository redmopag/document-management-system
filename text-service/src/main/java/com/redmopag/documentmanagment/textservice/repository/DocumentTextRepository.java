package com.redmopag.documentmanagment.textservice.repository;

import com.redmopag.documentmanagment.textservice.model.DocumentText;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DocumentTextRepository extends ElasticsearchRepository<DocumentText, String> {
    List<DocumentText> findByTextContaining(String text);

    Optional<DocumentText> findById(String id);

    void deleteAllByDocumentId(Long documentId);
}
