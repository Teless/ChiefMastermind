package com.exception;

public class ChiefMastermindRuntimeException extends RuntimeException {

    public ChiefMastermindRuntimeException(String message) {
        super(message);
    }

    public ChiefMastermindRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChiefMastermindRuntimeException(Throwable cause) {
        super(cause);
    }

}
