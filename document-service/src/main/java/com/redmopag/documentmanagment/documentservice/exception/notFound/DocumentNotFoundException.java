package com.redmopag.documentmanagment.documentservice.exception.notFound;

public class DocumentNotFoundException extends NotFoundException {
    public DocumentNotFoundException(Long id) {
        super("Документ с id " + id + " не найден");
    }
}
