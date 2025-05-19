package com.redmopag.documentmanagment.documentservice.handler;

import com.redmopag.documentmanagment.documentservice.dto.error.ErrorResponse;
import com.redmopag.documentmanagment.documentservice.exception.badrequest.BadRequestException;
import com.redmopag.documentmanagment.documentservice.exception.notFound.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }
}
