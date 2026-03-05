package org.example.service;

import org.example.model.Account;

public interface AccountService {
    String createAccount();
    Boolean logAccountAnCardNumber(long numberCard, int PINCard);
    String getBalance(long account);
}
