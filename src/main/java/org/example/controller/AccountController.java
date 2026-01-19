package org.example.controller;

import org.example.model.Account;
import org.example.service.AccountService;
import org.example.service.serviceImpl.AccountServiceImpl;


public class AccountController {

    private final AccountService accountService = new AccountServiceImpl();

    public String createAccount() {
        return accountService.createAccount();
    }

    /**
     * Определяет, что пришло на вход PIN или номер карты, и вызывает нужный метод.
     * @param numberCard номер
     * @return true или false в зависимости от результата
     */
    public Account loginOnNumberCard(Long numberCard, int PINCard) throws Exception {
            return accountService.logAccountAnCardNumber(numberCard, PINCard);
    }

    /**
     * Выводит баланс аккаунта.
     * @param account аккаунт
     * @return String баланс аккаунта.
     */
    public String getBalance(Account account) {
        return accountService.getBalanse(account);
    }
}
