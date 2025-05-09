package com.redmopag.documentmanagment.textservice.repository;

import com.redmopag.documentmanagment.textservice.model.DocumentText;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTextRepository extends ElasticsearchRepository<DocumentText, Long> {
    List<DocumentText> findByTextContaining(String text);
}
