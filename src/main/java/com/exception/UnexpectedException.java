package com.exception;

/**
 * An unexpected exception that can't be handle by the system
 */
public class UnexpectedException extends ChiefMastermindRuntimeException {

    public UnexpectedException(String message) {
        super(message);
    }

}
