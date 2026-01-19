package org.example.model;

import java.util.Random;

public class Generator extends Random {

    private final static String MII = "4";
    private final static String BIN_VISA_FREEDOM = MII + "00000";
    private static int ACCOUNT_IDENTIFIER  = 0;

    private final static int CHECKSUM_MAX = 10;

    private final static int PIN_LOWER_LIMIT_VALUES = 1000;
    private final static int PIN_UPPER_LIMIT_VALUES = 9999;


    /**
     * Генерирует номер карты и инкремент ACCOUNT_IDENTIFIER.
     * @return номер карты.
     */
    static Long numberCard(){
        String numberCard = BIN_VISA_FREEDOM +
                String.format("%09d", ACCOUNT_IDENTIFIER) +
                Generator.CHECKSUM_MAX;

        incrementAccountIdentifier();
        return Long.valueOf(numberCard);
    }

    /**
     * Генерирует PIN для карты.
     * @return PIN.
     */
    static synchronized int PINCard(){
        return new Random().nextInt(PIN_UPPER_LIMIT_VALUES - PIN_LOWER_LIMIT_VALUES + 1) + PIN_LOWER_LIMIT_VALUES;
    }

    /**
     * Инкремент ACCOUNT_IDENTIFIER.
     */
    private static synchronized void incrementAccountIdentifier(){
        ACCOUNT_IDENTIFIER++;
    }


}
