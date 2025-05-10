package com.redmopag.documentmanagment.documentservice.service.text;

import com.redmopag.documentmanagment.common.TextResponse;
import com.redmopag.documentmanagment.documentservice.client.TextClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextServiceImpl implements TextService {
    private final TextClient textClient;

    public TextServiceImpl(TextClient textClient) {
        this.textClient = textClient;
    }

    @Override
    public List<TextResponse> getTextByContaining(String text) {
        return textClient.search(text);
    }

    @Override
    public TextResponse getTextById(String id) {
        return textClient.getDocumentTextById(id);
    }
}
