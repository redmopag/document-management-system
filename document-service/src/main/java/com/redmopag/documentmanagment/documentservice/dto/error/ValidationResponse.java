package com.redmopag.documentmanagment.documentservice.dto.error;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResponse {
    private Map<String, String> errors;
}
