package com.redmopag.documentmanagment.documentservice.repository;

import com.redmopag.documentmanagment.documentservice.model.DocumentText;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTextRepository extends ElasticsearchRepository<DocumentText, Long> {
    List<DocumentText> findByTextContaining(String text);
}
