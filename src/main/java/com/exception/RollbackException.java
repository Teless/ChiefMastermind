package com.exception;

/**
 * Exception while rollingback some operation on the datasource
 */
public class RollbackException extends ChiefMastermindRuntimeException {

    public RollbackException(String message, Throwable cause) {
        super(message, cause);
    }
}
