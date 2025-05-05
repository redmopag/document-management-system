package com.redmopag.documentmanagment.ocrservice.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleOcrRequests {
    List<OcrRequest> requests;
}
