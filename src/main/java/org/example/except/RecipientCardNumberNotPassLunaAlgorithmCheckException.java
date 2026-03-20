package org.example.except;

public class RecipientCardNumberNotPassLunaAlgorithmCheckException extends RuntimeException {
    public RecipientCardNumberNotPassLunaAlgorithmCheckException() {
        super("Probably you made a mistake in the card number. Please try again!");
    }

    public RecipientCardNumberNotPassLunaAlgorithmCheckException(String message) {
        super(message);
    }
}
