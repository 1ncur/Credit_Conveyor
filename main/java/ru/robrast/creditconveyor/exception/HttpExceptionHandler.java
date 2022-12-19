package ru.robrast.creditconveyor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@SuppressWarnings("unused")
@ControllerAdvice
public class HttpExceptionHandler {
    @ExceptionHandler(RejectionException.class)
    public ResponseEntity<String> HandleRejection(RejectionException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
