package com.redmopag.documentmanagment.filestorageservice.exception;

public class NotAllowedStatusException extends BadRequestException {
    public NotAllowedStatusException(String message) {
        super(message);
    }
}
