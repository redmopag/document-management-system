package com.redmopag.documentmanagment.documentservice.exception.badrequest;

public abstract class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
