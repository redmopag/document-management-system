package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.common.*;

public interface OcrService {
    ProcessTextEvent recognize(OcrFileEvent event);
}
