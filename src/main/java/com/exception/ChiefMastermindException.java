package com.exception;

public class ChiefMastermindException extends Exception {

    public ChiefMastermindException(String message) {
        super(message);
    }

    public ChiefMastermindException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChiefMastermindException(Throwable cause) {
        super(cause);
    }

}
