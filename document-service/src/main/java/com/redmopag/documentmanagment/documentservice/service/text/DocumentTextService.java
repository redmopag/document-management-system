package com.redmopag.documentmanagment.documentservice.service.text;

import com.redmopag.documentmanagment.documentservice.model.*;

import java.util.List;

public interface DocumentTextService {
    void saveText(Long documentId, String text, String hocrContent);

    List<DocumentText> getTextsByContaining(String text);

    DocumentText getTextById(Long documentId);
}
