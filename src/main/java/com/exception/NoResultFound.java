package com.exception;

public class NoResultFound extends ChiefMastermindException {

    public NoResultFound(String entityName, String id) {
        super("No " + entityName + " with id: " + id + " was found");
    }

}
