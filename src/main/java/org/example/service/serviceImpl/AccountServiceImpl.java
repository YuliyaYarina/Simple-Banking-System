package org.example.service.serviceImpl;

import org.example.except.*;
import org.example.model.Account;
import org.example.model.Generator;
import org.example.repository.CardRepository;
import org.example.repository.CardRepositoryImpl;
import org.example.security.PinHasher;
import org.example.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record AccountServiceImpl(CardRepository cardRepository, Generator generator) implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    public AccountServiceImpl() {
        this(new CardRepositoryImpl(), new Generator());
    }

    @Override
    public String createAccount() {
        try {
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
        } catch (RuntimeException e) {
            return "\nSomething went wrong!";
        }
    }

    @Override
    public boolean isPinValid(String cardNumber, String pin) throws RecipientCardNumberNotExistException {
        Account account = cardRepository.findCard(cardNumber);
        return account != null && PinHasher.verify(pin, account.getPin());
    }

    @Override
    public String getBalance(String cardNumber) {
        return "\nBalance: " + cardRepository.getBalance(cardNumber);
    }

    @Override
    public boolean deleteAccount(String cardNumber) {
        try {
            boolean deleted = cardRepository.deleteAccount(cardNumber);
            if (deleted) {
                try {
                    Account account = cardRepository.findCard(cardNumber);
                    return account == null;
                } catch (RecipientCardNumberNotExistException e) {
                    return true;
                }
            } else return false;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addIncome(String income, String cardNumber) {
        try {
            return cardRepository.addIncome(income, cardNumber);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean isCardNumberValid(String cardNumber) throws RecipientCardNumberNotExistException {
        return cardRepository.findCard(cardNumber) != null;
    }

    @Override
    public boolean doTransfer(String cardNumber, String cardNumberDoTransfer, String money) throws NotEnoughMoneyInAccountException {
        try {
            if (cardRepository.getBalance(cardNumber) >= Long.parseLong(money)) {
                boolean doTransfer = cardRepository.setBalance(cardNumber, money);
                if (doTransfer) return addIncome(money, cardNumberDoTransfer);
                return true;
            } else {
                throw new NotEnoughMoneyInAccountException();
            }
        } catch (RuntimeException e) {
            return false;
        }
    }
}