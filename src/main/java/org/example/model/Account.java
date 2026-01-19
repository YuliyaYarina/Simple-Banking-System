package org.example.model;

import java.util.Objects;
/**
 * Представляет банковский счет с уникальным номером карты, ПИН-кодом и балансом.
 */
public class Account {

    /**
     * Уникальный номер карты.
     */
    private final long numberCard;

    /**
     * Личный идентификационный номер (ПИН-код) для счета.
     */
    private final int PINCard;

    /**
     * Текущий баланс счета.
     */
    private long balance;

    /**
     * Начальный баланс для нового счета, который равен нулю.
     */
    private final static long ZERO_BALANCE = 0;

    /**
     * Создает новый счет с случайно сгенерированным номером карты и ПИН-кодом, и начальным балансом равным нулю.
     */
    public Account() {
        this.numberCard = Generator.numberCard();
        this.PINCard = Generator.PINCard();
        this.balance = ZERO_BALANCE;
    }

    /**
     * Возвращает уникальный номер карты счета.
     * @return номер карты
     */
    public long getNumberCard() {
        return numberCard;
    }

    /**
     * Возвращает ПИН-код счета.
     * @return ПИН-код
     */
    public int getPINCard() {
        return PINCard;
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
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(numberCard, account.numberCard) && Objects.equals(PINCard, account.PINCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberCard, PINCard);
    }

    @Override
    public String toString() {
        return "Account{" +
                "numberCard='" + numberCard + '\'' +
                ", PINCard=" + PINCard +
                ", balance=" + balance +
                '}';
    }
}
