package org.example.model;

import java.util.Objects;
/**
 * Представляет банковский счет с уникальным номером карты, ПИН-кодом и балансом.
 */
public class Account {
    /**
     * Уникальный номер карты.
     */
    private final long cardNumber;

    /**
     * Личный идентификационный номер (ПИН-код) для счета.
     */
    private final String pin;

    /**
     * Текущий баланс счета.
     */
    private long balance;

    /**
     * Начальный баланс для нового счета, который равен нулю.
     */
    private final static long ZERO_BALANCE = 0;

    /**
     * Создает новый счет случайно сгенерированным номером карты и ПИН-кодом, и начальным балансом равным нулю.
     */
    public Account(Long numberCard, String pin) {
        this.cardNumber = numberCard;
        this.pin = pin;
        this.balance = ZERO_BALANCE;
    }

    /**
     * Возвращает уникальный номер карты счета.
     * @return номер карты
     */
    public long getCardNumber() {
        return cardNumber;
    }

    /**
     * Возвращает ПИН-код счета.
     * @return ПИН-код
     */
    public String getPin() {
        return pin;
    }

    /**
     * Возвращает текущий баланс счета.
     * @return баланс
     */
    public long getBalance() {
        return balance;
    }

    /**
     * Устанавливает баланс счета на указанное значение.
     * @param balance новый баланс
     */
    public void setBalance(long balance) {
        this.balance = balance;
    }

    public boolean isPinCorrect(String pinToTest){
        return this.pin.equals(pinToTest);
    }

    /**
     * Проверяет, равен ли этот счет указанному объекту.
     * Два счета считаются равными, если у них одинаковый номер карты и ПИН-код.
     * @param o объект для сравнения
     * @return true, если объекты равны, false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(cardNumber, account.cardNumber) && Objects.equals(pin, account.pin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, pin);
    }

    @Override
    public String toString() {
        return "Account{" +
                "numberCard='" + cardNumber + '\'' +
                ", PINCard=" + pin +
                ", balance=" + balance +
                '}';
    }
}
