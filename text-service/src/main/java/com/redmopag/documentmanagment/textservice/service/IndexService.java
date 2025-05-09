package com.redmopag.documentmanagment.textservice.service;

import com.redmopag.documentmanagment.common.*;
import com.redmopag.documentmanagment.textservice.dto.*;

public interface IndexService {
    MetadataEvent processText(ProcessTextEvent processTextEvent);
}
