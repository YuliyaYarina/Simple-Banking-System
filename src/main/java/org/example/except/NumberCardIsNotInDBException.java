package org.example.except;

public class NumberCardIsNotInDBException extends RuntimeException {
    public NumberCardIsNotInDBException(String message) {
        super(message);
    }

    public NumberCardIsNotInDBException() {
    }
}
