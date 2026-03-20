package org.example.service;

public interface LuhnAlgorithmService {

    /**
     * Метод пропускает номер сгенерированный номер карты(без последней цифры) через алгоритм Луна, и добавляет крайний номер карты, соответствующий алгоритму Луна.
     * @param numberCard номер карты.
     * @return номер карты соответствующий алгоритму Луна.
     */
    String generateLuhnNumber(String numberCard);

    /**
     * Убирает крайнюю цифру
     * @param cardNumber номер карты.
     * @return номер карты, без крайней цифры.
     */
    String discardsLastDigitOfCardNumber(String cardNumber);

    /**
     * Умножает нечетные числа в строке
     * @param numberCard первые 15 чисел номера карты
     * @return новый номер, с умноженными нечетными числами.
     */
    int[] multiplyOddDigitsByTwo(String[] numberCard);

    /**
     * Перебираем номер и из чисел >9 вычитаем 9.
     * @param numberCard номер карты, без крайней цифры.
     * @return новый номер, с числами больше 9, вычтено 9.
     */
    int[] subtractNumbersOver9(int[] numberCard);

    /**
     * Вычисляем и возвращаем по алгоритму луна крайнюю цифру.
     * @param numberCard номер карты, без крайней цифры.
     * @return крайняя цифра номера карты.
     */
    int calculateCheckDigit(int[] numberCard);
}
