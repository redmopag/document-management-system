package com.redmopag.documentmanagment.documentservice.service.text;

import com.redmopag.documentmanagment.common.TextResponse;

import java.util.List;

public interface TextService {
    List<TextResponse> getTextByContaining(String text);

    TextResponse getTextById(String id);
}
