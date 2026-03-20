package org.example.service.serviceImpl;

import org.example.service.LuhnAlgorithmService;

public class LuhnAlgorithmServiceImpl implements LuhnAlgorithmService {
    /**
     * Получает строку, переносит в массив, и выполняет по очереди операции алгоритма Луна.
     * @param numberCard номер карты.
     * @return номер карты из 16 цифр, соответствующий методу луна
     */
    @Override
    public String generateLuhnNumber(String numberCard) {
        if(numberCard.length() == 16){
            numberCard = discardsLastDigitOfCardNumber(numberCard);
        }
        int[] digits = multiplyOddDigitsByTwo(numberCard.split(""));
        digits = subtractNumbersOver9(digits);

        return numberCard + calculateCheckDigit(digits);
    }

    @Override
    public String discardsLastDigitOfCardNumber(String cardNumber) {
        StringBuilder discardedCardNumber =  new StringBuilder();
        for (int i = 0; i < cardNumber.length()-1; i++) {
            discardedCardNumber.append(cardNumber.charAt(i));
        }
       return discardedCardNumber.toString();
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

    @Override
    public int[] subtractNumbersOver9(int[] numberCard) {
        int[] transformed = new int[numberCard.length];
        for (int i = 0; i < numberCard.length; i++) {
            transformed[i] = numberCard[i] > 9 ? numberCard[i] - 9 : numberCard[i];
        }
        return transformed;
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
}
