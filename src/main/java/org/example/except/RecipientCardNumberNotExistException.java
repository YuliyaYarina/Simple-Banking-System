package org.example.except;

public class RecipientCardNumberNotExistException extends RuntimeException {
    public RecipientCardNumberNotExistException() {
        super("Such a card does not exist.");
    }

    public RecipientCardNumberNotExistException(String message) {
        super(message);
    }
}
