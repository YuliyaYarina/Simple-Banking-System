package org.example.service;

public interface AccountService {
    String createAccount();

    boolean loginByCardNumber(String cardNumber, String pin);

    String getBalance(String cardNumber);

    boolean deleteAccount(String cardNumber);
}
