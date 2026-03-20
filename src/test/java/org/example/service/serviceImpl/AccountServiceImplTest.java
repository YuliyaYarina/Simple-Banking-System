package org.example.service.serviceImpl;

import org.example.except.RecipientCardNumberNotExistException;
import org.example.model.Account;
import org.example.model.Generator;
import org.example.repository.CardRepository;
import org.example.security.PinHasher;
import org.example.service.AccountService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {
    @Test
    void loginShouldSucceedForValidPin() {
        InMemoryCardRepository repository = new InMemoryCardRepository();
        String card = "4000001234567890";
        String pin = "1234";
        repository.saveCard(card, PinHasher.hash(pin));

        AccountService service = new AccountServiceImpl(repository, new Generator());

        assertTrue(service.isPinValid(card, pin));
    }

    @Test
    void loginShouldFailForInvalidPin() {
        InMemoryCardRepository repository = new InMemoryCardRepository();
        String card = "4000001234567890";
        repository.saveCard(card, PinHasher.hash("1234"));

        AccountService service = new AccountServiceImpl(repository, new Generator());

        assertFalse(service.isPinValid(card, "9999"));
    }

    private static class InMemoryCardRepository implements CardRepository {

        private final Map<String, Account> accounts = new HashMap<>();

        @Override
        public void saveCard(String numberCard, String pinHash) {
            accounts.put(numberCard, new Account(numberCard, pinHash));
        }

        @Override
        public Account findCard(String numberCard) throws RecipientCardNumberNotExistException {
            Account account = accounts.get(numberCard);
            if (account == null) {
                throw new RecipientCardNumberNotExistException();
            }
            return account;
        }

        @Override
        public String getAccounts() {
            return accounts.toString();
        }

        @Override
        public String findMaxCardNumber() {
            return accounts.keySet().stream().max(String::compareTo).orElse(null);
        }

        @Override
        public boolean deleteAccount(String cardNumber) {
            accounts.remove(cardNumber);
            return accounts.containsKey(cardNumber);
        }

        @Override
        public boolean addIncome(String income, String cardNumber) {
            try {
                accounts.get(cardNumber).setBalance(Long.parseLong(accounts.get(cardNumber).getBalance() + income));
                return true;
            } catch (RuntimeException e){
                return false;
            }
        }

        @Override
        public long getBalance(String cardNumber) {
            return accounts.get(cardNumber).getBalance();
        }

        @Override
        public boolean setBalance(String cardNumber, String money) {
            if(accounts.get(cardNumber).getBalance() >= money.length()) {
                accounts.get(cardNumber).setBalance(accounts.get(cardNumber).getBalance() - Long.parseLong(money));
                return true;
            }
            return false;
        }
    }
}