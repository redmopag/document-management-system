package com.redmopag.documentmanagment.textservice.service;

import com.redmopag.documentmanagment.common.*;

import java.util.List;

public interface IndexService {
    MetadataEvent processText(ProcessTextEvent processTextEvent);

    List<TextResponse> search(String text);

    TextResponse getDocumentTextById(String id);
}
