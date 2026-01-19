package org.example.service;

import org.example.model.Account;

public interface AccountService {
    String createAccount();
    Account logAccountAnCardNumber(Long numberCard,  int PINCard) throws Exception;
        String getBalanse(Account account);
}
