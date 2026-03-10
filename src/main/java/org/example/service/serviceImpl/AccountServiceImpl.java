package org.example.service.serviceImpl;

import org.example.model.Account;
import org.example.model.Generator;
import org.example.repository.CardRepository;
import org.example.repository.CardRepositoryImpl;
import org.example.service.AccountService;

public class AccountServiceImpl implements AccountService {

    private final CardRepository cardRepository = new CardRepositoryImpl();
    private final Generator generator = new Generator();

    @Override
    public String createAccount() {
        String lastCardNumber = cardRepository.searchMaxNumberCard();
        Generator.incrementAccountIdentifier(lastCardNumber);

        Account account = new Account(generator.numberCard(), generator.PINCard());
        cardRepository.updatedCard(String.valueOf(account.getCardNumber()), account.getPin());

        return "\nYour card has been created\n"
                + "Your card number:\n"
                + account.getCardNumber()
                + "\nYour card PIN:\n"
                + account.getPin();
    }

    @Override
    public Boolean logAccountAnCardNumber(long accountNumber, String PINCard) {
        Account account = cardRepository.getCard(accountNumber);
        return PINCard.equals(account.getPin());
    }

    @Override
    public String getBalance(long numberCard) {
        Account account = cardRepository.getCard(numberCard);
        return "\nBalance: " + account.getBalance();
    }
}