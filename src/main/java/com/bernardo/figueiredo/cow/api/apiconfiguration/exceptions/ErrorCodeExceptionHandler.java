package com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorCodeExceptionHandler {

    @ExceptionHandler(ErrorCodeException.class)
    public ResponseEntity<String> handleCustomException(ErrorCodeException errorCodeException) {
        ErrorCode errorCode = errorCodeException.getErrorCode();
        return new ResponseEntity<>(errorCode.getErrorMessage(), errorCode.getResponseStatus());
    }
}
