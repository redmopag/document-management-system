package com.redmopag.documentmanagment.filestorageservice.exception;

public class InvalidFileException extends BadRequest {
    public InvalidFileException(String message) {
        super(message);
    }
}
