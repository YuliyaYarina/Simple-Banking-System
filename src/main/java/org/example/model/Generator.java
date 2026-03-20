package org.example.model;

import org.example.service.LuhnAlgorithmService;
import org.example.service.serviceImpl.LuhnAlgorithmServiceImpl;

import java.security.SecureRandom;

public class Generator {

    private static final String BIN = "400000";
    private static int accountIdentifier = 0;

    private static final int PIN_MIN = 0;
    private static final int PIN_MAX = 9999;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final LuhnAlgorithmService luhnAlgorithmService = new LuhnAlgorithmServiceImpl();

    /**
     * Генерирует номер карты и инкремент ACCOUNT_IDENTIFIER.
     * @return номер карты.
     */
    public synchronized String generateCardNumber() {
        String cardNumber = luhnAlgorithmService.generateLuhnNumber(BIN + String.format("%09d", accountIdentifier));
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
}