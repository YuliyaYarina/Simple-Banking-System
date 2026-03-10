package org.example.service.serviceImpl;

import org.example.model.Account;
import org.example.model.BankingSystem;
import org.example.model.Generator;
import org.example.repository.CardRepository;
import org.example.repository.CardRepositoryImpl;
import org.example.service.AccountService;

import java.sql.SQLException;

public class AccountServiceImpl implements AccountService {

    private final CardRepository  cardRepository = new CardRepositoryImpl();
    /**
     * Находит в БД самый наибольший номер карты, создает новый номер карты, добавляет в БД.
     * @return индивидуальный номер карты, согласно алгоритму Луна.
     */
    @Override //нужно добавить проверку на наличие созданного аккаунта в бд. а надо?? НАДО!!!
    public String createAccount() { // не доделан
        try {
            String lastCardNumber = cardRepository.searchMaxNumberCard();
            Generator.incrementAccountIdentifier(lastCardNumber);

            Account account = new Account(new Generator().numberCard(), new Generator().PINCard());
            BankingSystem.addAccount(account.getCardNumber(), account);

            cardRepository.updatedCard(String.valueOf(account.getCardNumber()), String.valueOf(account.getPin()));

            return "\nYour card has been created\n" +
                    "Your card number:\n" +
                    account.getCardNumber() +
                    "\nYour card PIN:\n" +
                    account.getPin();

        } catch (RuntimeException | SQLException e ) {
            Exception e1 = e;
            e1.printStackTrace();
            return e1.getMessage();
        }
    }

    /**
     * Находит аккаунт по номеру карты, и сравнивает их PIN.
     *
     * @param accountNumber номер карты.
     * @param PINCard       PIN карты.
     * @return найденный аккаунт, или null.
     */
    @Override
    public Boolean logAccountAnCardNumber(long accountNumber, String PINCard) {
        Account account = cardRepository.getCard(accountNumber);
        return PINCard.equals(account.getPin());
    }

    @Override
    public String getBalance(long numberCard) {
        Account account =  cardRepository.getCard(numberCard);
        return "\nBalance: " + account.getBalance();
    }
}
