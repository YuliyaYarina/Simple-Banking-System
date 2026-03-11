package org.example.service.serviceImpl;

import org.example.model.Account;
import org.example.model.Generator;
import org.example.repository.CardRepository;
import org.example.repository.CardRepositoryImpl;
import org.example.security.PinHasher;
import org.example.service.AccountService;

public class AccountServiceImpl implements AccountService {

    private final CardRepository cardRepository;
    private final Generator generator;

    public AccountServiceImpl() {
        this(new CardRepositoryImpl(), new Generator());
    }

    public AccountServiceImpl(CardRepository cardRepository, Generator generator) {
        this.cardRepository = cardRepository;
        this.generator = generator;
    }

    @Override
    public String createAccount() {
        String lastCardNumber = cardRepository.findMaxCardNumber();
        Generator.incrementAccountIdentifier(lastCardNumber);

        String cardNumber = generator.generateCardNumber();
        String pin = generator.generatePin();
        String pinHash = PinHasher.hash(pin);

        cardRepository.saveCard(cardNumber, pinHash);

        return "\nYour card has been created\n"
                + "Your card number:\n"
                + cardNumber
                + "\nYour card PIN:\n"
                + pin;
    }

    @Override
    public boolean loginByCardNumber(String cardNumber, String pin) {
        Account account = cardRepository.findCard(cardNumber);
        return PinHasher.verify(pin, account.getPin());
    }

    @Override
    public String getBalance(String cardNumber) {
        Account account = cardRepository.findCard(cardNumber);
        return "\nBalance: " + account.getBalance();
    }
}