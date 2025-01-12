package com.blogging.project.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AppError> entityNotFoundExceptionHandler(EntityNotFoundException ex){
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalError.class)
    public ResponseEntity<AppError> internalServerErrorHandler(InternalError error){
        log.error(error.getMessage(), error);
        AppError appError = new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), error.getMessage());
        return new ResponseEntity<>(appError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
