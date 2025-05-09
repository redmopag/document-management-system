package com.redmopag.documentmanagment.documentservice.exception;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(Long id) {
        super("Документ с id " + id + " не найден");
    }
}
