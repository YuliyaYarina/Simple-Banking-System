package org.example.model;

import java.security.SecureRandom;

public class Generator extends SecureRandom implements LuhnAlgorithm {

    private static final String BIN = "400000";
    private static int ACCOUNT_IDENTIFIER = 0;

    private static final int PIN_LOWER_LIMIT_VALUES = 0;
    private static final int PIN_UPPER_LIMIT_VALUES = 9999;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Генерирует номер карты и инкремент ACCOUNT_IDENTIFIER.
     * @return номер карты.
     */
    public Long numberCard() {
        String numberCard = generatedLuhnAlgorithm(BIN + String.format("%09d", ACCOUNT_IDENTIFIER));
        incrementAccountIdentifier(null);
        return Long.valueOf(numberCard);
    }

    /**
     * Генерирует PIN для карты.
     * @return PIN.
     */
    public synchronized String PINCard() {
        int pinValue = RANDOM.nextInt(PIN_UPPER_LIMIT_VALUES - PIN_LOWER_LIMIT_VALUES + 1) + PIN_LOWER_LIMIT_VALUES;
        return String.format("%04d", pinValue);
    }

    /**
     * Метод получает крайний созданный номер карты, и увеличивает это число.
     */
    public static synchronized void incrementAccountIdentifier(String lastCardNumber) {
        if(lastCardNumber != null) {
            String[] cardNumberLast = new String[9];
            int t = 0;
            for (int i = 6; i < 15; i++){
                cardNumberLast[t] = lastCardNumber.split("")[i];
                t++;
            }
            StringBuilder transformsToString = new StringBuilder();

            for (String s : cardNumberLast) {
                transformsToString.append(s);
            }
            ACCOUNT_IDENTIFIER = Integer.parseInt(transformsToString.toString());
            ACCOUNT_IDENTIFIER++;
        } else {
            ACCOUNT_IDENTIFIER++;
        }
    }


    // методы луна нужно перенести в отдельный class

    /**
     * Получает строку, переносит в массив, и выполняет по очереди операции алгоритма Луна.
     * @param numberCard номер карты, без крайней цифры.
     * @return номер карты, соответствующий методу луна
     */
    @Override
    public synchronized String generatedLuhnAlgorithm(String numberCard) {
        int[] newNumberCard;
        String[] numberCards = numberCard.split("");

        newNumberCard = multipedOddDigitsBy2(numberCards);
        newNumberCard = subtractNumbersOver9(newNumberCard);

        return numberCard + addAllNumbers(newNumberCard);
    }

    @Override
    public int addAllNumbers(int[] numberCard) {
        int sum = 0;
        for (int j : numberCard) {
            sum += j;
        }
        sum = sum % 10;
        return sum != 0 ? 10 - sum : 0;
    }

    @Override
    public int[] subtractNumbersOver9(int[] numberCard) {
        int[] newNumberCard = new int[15];
        for (int i = 0; i < numberCard.length; i++) {
            if (numberCard[i] > 9) {
                newNumberCard[i] = numberCard[i] - 9;
            } else {
                newNumberCard[i] = numberCard[i];
            }
        }
        return newNumberCard;
    }

    @Override
    public int[] multipedOddDigitsBy2(String[] numberCard) {
        int[] multipOddDigitsBy2 = new int[15];

        for (int i = 0 ; i < numberCard.length; i++) {
            int number = Integer.parseInt(numberCard[i]);
            if (i % 2 == 0) {
                multipOddDigitsBy2[i] = number * 2;
            } else {
                multipOddDigitsBy2[i] = number;
            }
        }
        return multipOddDigitsBy2;
    }
}