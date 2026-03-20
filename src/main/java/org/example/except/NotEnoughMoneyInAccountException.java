package org.example.except;

public class NotEnoughMoneyInAccountException extends RuntimeException {
    public NotEnoughMoneyInAccountException() {
        super("Not enough money!");
    }

    public NotEnoughMoneyInAccountException(String message) {
        super(message);
    }
}
