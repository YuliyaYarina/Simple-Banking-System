package org.example.model;

import java.util.Random;

public class Generator extends Random implements LuhnAlgorithm {

    private final static String MII = "4";
    private final static String BIN_VISA_FREEDOM = MII + "00000";
    private static int ACCOUNT_IDENTIFIER  = 0;

    private final static int PIN_LOWER_LIMIT_VALUES = 1000;
    private final static int PIN_UPPER_LIMIT_VALUES = 9999;


    /**
     * Генерирует номер карты и инкремент ACCOUNT_IDENTIFIER.
     * @return номер карты.
     */
    Long numberCard(){
        String numberCard = generatedLuhnAlgorithm(BIN_VISA_FREEDOM +
                String.format("%09d", ACCOUNT_IDENTIFIER));

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

    @Override
    public synchronized String generatedLuhnAlgorithm(String numberCard){
        int[] newNumberCard ;
        String[] numberCards = numberCard.split("");

        newNumberCard = multipedOddDigitsBy2(numberCards);
        newNumberCard = subtractNumbersOver9(newNumberCard);

        return numberCard + addAllNumbers(newNumberCard);
    }

    @Override
    public int addAllNumbers(int[] numberCard){
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
            if(numberCard[i] > 9) {
                newNumberCard[i] =  numberCard[i] - 9;
            }else {
                newNumberCard[i] = numberCard[i];
            }
        }
        return newNumberCard;
    }

    @Override
    public int[] multipedOddDigitsBy2(String[] numberCard){
        int[] multipOddDigitsBy2 = new int[15];

        for(int i = 0 ; i < numberCard.length; i++){
            int number = Integer.parseInt(numberCard[i]);
            if(i % 2 == 0) {
                multipOddDigitsBy2[i] = number * 2;
            }else {
                multipOddDigitsBy2[i] = number;
            }
        }
        return multipOddDigitsBy2;
    }
}
