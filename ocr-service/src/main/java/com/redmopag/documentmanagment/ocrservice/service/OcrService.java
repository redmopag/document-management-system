package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.common.*;
import com.redmopag.documentmanagment.ocrservice.dto.*;

public interface OcrService {
    ProcessTextEvent recognize(OcrFileEvent event);
}
