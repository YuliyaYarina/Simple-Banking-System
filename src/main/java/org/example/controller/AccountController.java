package org.example.controller;

import org.example.except.NotEnoughMoneyInAccountException;
import org.example.except.RecipientCardNumberNotExistException;
import org.example.except.RecipientCardNumberNotPassLunaAlgorithmCheckException;
import org.example.except.TransferToSameAccountException;
import org.example.service.AccountService;
import org.example.service.LuhnAlgorithmService;
import org.example.service.serviceImpl.AccountServiceImpl;
import org.example.service.serviceImpl.LuhnAlgorithmServiceImpl;


public class AccountController {

    private final AccountService accountService =  new AccountServiceImpl();
    private final LuhnAlgorithmService luhnAlgorithmService =  new LuhnAlgorithmServiceImpl();

    public String createAccount() {
        return accountService.createAccount();
    }

    public boolean isPinValid(String cardNumber, String pin) throws NullPointerException {
        return accountService.isPinValid(cardNumber, pin);
    }

    /**
     * Выводит баланс аккаунта.
     */
    public void getBalance(String cardNumber) {
        System.out.println(accountService.getBalance(cardNumber));
    }

    public boolean deleteAccount(String cardNumber) {
        return accountService.deleteAccount(cardNumber);
    }

    public boolean addIncome(String income, String cardNumber) {
        return accountService.addIncome(income, cardNumber);
    }

    public boolean isCardNumberValid(String cardNumber, String cardNumberDoTransfer) {
        try {
            if (cardNumberDoTransfer.equals(cardNumber)) throw new TransferToSameAccountException();
            if (!luhnAlgorithmService.generateLuhnNumber(cardNumber).equals(cardNumber)) throw new RecipientCardNumberNotPassLunaAlgorithmCheckException();

            return accountService.isCardNumberValid(cardNumber);
        } catch (RecipientCardNumberNotPassLunaAlgorithmCheckException | RecipientCardNumberNotExistException |
                 TransferToSameAccountException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean doTransfer(String cardNumber,String cardNumberDoTransfer, String money) {
        try {
            return accountService.doTransfer(cardNumber, cardNumberDoTransfer, money);
        } catch (NotEnoughMoneyInAccountException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
