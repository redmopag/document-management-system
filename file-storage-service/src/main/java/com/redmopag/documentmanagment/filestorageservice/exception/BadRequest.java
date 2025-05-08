package com.redmopag.documentmanagment.filestorageservice.exception;

public abstract class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super(message);
    }
}
