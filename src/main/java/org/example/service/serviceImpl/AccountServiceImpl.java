package org.example.service.serviceImpl;

import org.example.model.Account;
import org.example.model.BankingSystem;
import org.example.service.AccountService;


public class AccountServiceImpl implements AccountService {

    @Override
    public String createAccount() {
        Account account = new Account();
        BankingSystem.addAccount(account.getNumberCard(), account);

        return "\nYour card has been created\n" +
                "Your card number:\n" +
                account.getNumberCard() +
                "\nYour card PIN:\n" +
                account.getPINCard();
    }

    /**
     * Находит аккаунт по номеру карты, и сравнивает их PIN.
     * @param account номер карты.
     * @param PINCard PIN карты.
     * @return найденный аккаунт, или null.
     *
     */
    @Override
    public Account logAccountAnCardNumber(Long account, int PINCard) throws Exception {
        Account result = BankingSystem.getAccount(account);
        return PINCard == result.getPINCard() ? result : null;
    }

    @Override
    public String getBalanse(Account account) {
        return "\nBalance: " + account.getBalance();
    }

}
