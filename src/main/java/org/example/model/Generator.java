package org.example.model;

import java.security.SecureRandom;

public class Generator implements LuhnAlgorithm {

    private static final String BIN = "400000";
    private static int accountIdentifier = 0;

    private static final int PIN_MIN = 0;
    private static final int PIN_MAX = 9999;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Генерирует номер карты и инкремент ACCOUNT_IDENTIFIER.
     * @return номер карты.
     */
    public synchronized String generateCardNumber() {
        String cardNumber = generateLuhnNumber(BIN + String.format("%09d", accountIdentifier));
        incrementAccountIdentifier(null);
        return cardNumber;
    }

    /**
     * Генерирует PIN для карты.
     * @return PIN.
     */
    public synchronized String generatePin() {
        int pinValue = RANDOM.nextInt(PIN_MAX - PIN_MIN + 1) + PIN_MIN;
        return String.format("%04d", pinValue);
    }

    /**
     * Метод получает крайний созданный номер карты, и увеличивает это число.
     */
    public static synchronized void incrementAccountIdentifier(String lastCardNumber) {
        if (lastCardNumber != null) {
            String identifier = lastCardNumber.substring(6, 15);
            accountIdentifier = Integer.parseInt(identifier) + 1;
        } else {
            accountIdentifier++;
        }
    }

    /**
     * Получает строку, переносит в массив, и выполняет по очереди операции алгоритма Луна.
     * @param numberCardWithoutCheckDigit номер карты, без крайней цифры.
     * @return номер карты, соответствующий методу луна
     */
    @Override
    public String generateLuhnNumber(String numberCardWithoutCheckDigit) {
        int[] digits = multiplyOddDigitsByTwo(numberCardWithoutCheckDigit.split(""));
        digits = subtractNumbersOver9(digits);
        return numberCardWithoutCheckDigit + calculateCheckDigit(digits);
    }

    @Override
    public int calculateCheckDigit(int[] numberCard) {
        int sum = 0;
        for (int digit : numberCard) {
            sum += digit;
        }
        int mod = sum % 10;
        return mod == 0 ? 0 : 10 - mod;
    }

    @Override
    public int[] subtractNumbersOver9(int[] numberCard) {
        int[] transformed = new int[numberCard.length];
        for (int i = 0; i < numberCard.length; i++) {
            transformed[i] = numberCard[i] > 9 ? numberCard[i] - 9 : numberCard[i];
        }
        return transformed;
    }

    @Override
    public int[] multiplyOddDigitsByTwo(String[] numberCard) {
        int[] multiplied = new int[numberCard.length];
        for (int i = 0; i < numberCard.length; i++) {
            int number = Integer.parseInt(numberCard[i]);
            multiplied[i] = i % 2 == 0 ? number * 2 : number;
        }
        return multiplied;
    }
}