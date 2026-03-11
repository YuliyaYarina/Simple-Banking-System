package org.example.repository;

import org.example.model.Account;


public interface CardRepository {

    void saveCard(String numberCard, String pinHash);

    Account findCard(String numberCard);

    String getAccounts();

    String findMaxCardNumber();
}
