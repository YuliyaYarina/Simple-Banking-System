package org.example.except;

public class TransferToSameAccountException extends RuntimeException {
    public TransferToSameAccountException() {
        super("You can't transfer money to the same account!");
    }

    public TransferToSameAccountException(String message) {
        super(message);
    }
}
