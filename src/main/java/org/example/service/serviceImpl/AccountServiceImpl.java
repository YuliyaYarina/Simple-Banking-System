package org.example.service.serviceImpl;

import org.example.except.*;
import org.example.model.Account;
import org.example.model.Generator;
import org.example.repository.CardRepository;
import org.example.repository.CardRepositoryImpl;
import org.example.security.PinHasher;
import org.example.service.AccountService;

public record AccountServiceImpl(CardRepository cardRepository, Generator generator) implements AccountService {

    public AccountServiceImpl() {
        this(new CardRepositoryImpl(), new Generator());
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
    public boolean isPinValid(String cardNumber, String pin) {
        Account account = cardRepository.findCard(cardNumber);
        return PinHasher.verify(pin, account.getPin());
    }

    @Override
    public String getBalance(String cardNumber) {
        return "\nBalance: " + cardRepository.getBalance(cardNumber);
    }

    @Override
    public boolean deleteAccount(String cardNumber) {
        return cardRepository.deleteAccount(cardNumber);
    }

    @Override
    public boolean addIncome(String income, String cardNumber) {
        long amount = Long.parseLong(income);
        if (amount < 0) {
            return false;
        }
        return cardRepository.addIncome(income, cardNumber);
    }

    @Override
    public boolean isCardNumberValid(String cardNumber) {
        return cardRepository.findCard(cardNumber) != null;
    }

    @Override
    public boolean doTransfer(String cardNumber, String cardNumberDoTransfer, String money)
            throws NotEnoughMoneyInAccountException {
        long amount = Long.parseLong(money);
        if (amount <= 0) {
            return false;
        }

        boolean doTransfer = cardRepository.transferMoney(cardNumber, cardNumberDoTransfer, amount);
        if (!doTransfer) {
            throw new NotEnoughMoneyInAccountException();
        }
        return true;
    }
}