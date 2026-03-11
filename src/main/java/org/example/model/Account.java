package org.example.model;

import java.util.Objects;
/**
 * Представляет банковский счет с номером карты, PIN (или его хешем) и балансом.
 */
public class Account {

    private final static long ZERO_BALANCE = 0;

    private final String cardNumber;
    private final String pin;
    private long balance;

    public Account(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = ZERO_BALANCE;
    }

    /**
     * Возвращает уникальный номер карты.
     * @return номер карты
     */
    public String getCardNumber() {
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

    /**
     * Проверяет, равен ли этот счет указанному объекту.
     * Два счета считаются равными, если у них одинаковый номер карты и ПИН-код.
     * @param o объект для сравнения
     * @return true, если объекты равны, false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return balance == account.balance
                && Objects.equals(cardNumber, account.cardNumber)
                && Objects.equals(pin, account.pin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, pin, balance);
    }
}
