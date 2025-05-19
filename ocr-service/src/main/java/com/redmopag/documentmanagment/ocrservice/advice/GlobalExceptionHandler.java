package com.redmopag.documentmanagment.ocrservice.advice;

import com.redmopag.documentmanagment.ocrservice.dto.ErrorResponse;
import com.redmopag.documentmanagment.ocrservice.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFileException() {
        return new ErrorResponse("Не удалось прочитать файл");
    }

    @ExceptionHandler(OcrFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOcrFailedException() {
        return new ErrorResponse("Не удалось распознать текст");
    }
}
