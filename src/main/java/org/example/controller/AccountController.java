package org.example.controller;

import org.example.service.AccountService;
import org.example.service.serviceImpl.AccountServiceImpl;


public class AccountController {

    private final AccountService accountService;

    public AccountController() {
        this(new AccountServiceImpl());
    }

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public String createAccount() {
        return accountService.createAccount();
    }

    public boolean isPinValid(String cardNumber, String pin) {
        return accountService.loginByCardNumber(cardNumber, pin);
    }

    /**
     * Выводит баланс аккаунта.
     * @return String баланс аккаунта.
     */
    public String getBalance(String cardNumber) {
        return accountService.getBalance(cardNumber);
    }
}
