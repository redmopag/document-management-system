package com.redmopag.documentmanagment.filestorageservice.exception;

public class InvalidFileException extends BadRequestException {
    public InvalidFileException(String message) {
        super(message);
    }
}
