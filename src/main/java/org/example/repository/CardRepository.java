package org.example.repository;

import org.example.model.Account;


public interface CardRepository {

    void saveCard(String numberCard, String pinHash);

    Account findCard(String numberCard);

    String getAccounts();

    String findMaxCardNumber();

    boolean deleteAccount(String cardNumber);

    boolean addIncome(String income, String cardNumber);

    long getBalance(String cardNumber);

    boolean setBalance(String cardNumber, String money);
}
