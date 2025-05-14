package com.redmopag.documentmanagment.documentservice.dto.error;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResponse {
    private List<String> errors;
}
