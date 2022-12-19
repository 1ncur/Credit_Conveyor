package ru.robrast.creditconveyor.exception;

import lombok.Data;




@Data
public class RejectionException extends RuntimeException{

    private final String message;

    public RejectionException(String message) {
        this.message = message;
    }
}
