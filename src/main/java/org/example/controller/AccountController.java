package org.example.controller;

import org.example.service.AccountService;
import org.example.service.serviceImpl.AccountServiceImpl;


public class AccountController {

    private final AccountService accountService = new AccountServiceImpl();

    public String createAccount() {
        return accountService.createAccount();
    }

    /**
     * Сравнивает PIN.
     * @param numberCard номер
     * @param PINCard PIN
     * @return true или false в зависимости от результата
     */
    public Boolean equalsPIN(long numberCard, String PINCard) {
            return accountService.logAccountAnCardNumber(numberCard, PINCard);
    }

    /**
     * Выводит баланс аккаунта.
     * @param account аккаунт
     * @return String баланс аккаунта.
     */
    public String getBalance(long account) {
        return accountService.getBalance(account);
    }
}
