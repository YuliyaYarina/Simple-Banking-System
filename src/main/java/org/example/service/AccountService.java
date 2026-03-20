package org.example.service;

public interface AccountService {
    String createAccount();

    boolean isPinValid(String cardNumber, String pin);

    String getBalance(String cardNumber);

    boolean deleteAccount(String cardNumber);

    boolean addIncome(String income, String cardNumber);

    boolean isCardNumberValid(String cardNumber);

    boolean doTransfer(String cardNumber, String cardNumberDoTransfer, String money);
}
