package org.example.repository;

import org.example.model.Account;

import java.sql.SQLException;

public interface CardRepository {

    void updatedCard(String numberCard, String pin) throws SQLException;

    Account getCard(long numberCard);

    String getAccounts();

    String searchMaxNumberCard();
}
