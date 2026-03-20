package org.example.service.serviceImpl;

import org.example.except.NotEnoughMoneyInAccountException;
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

    @Test
    void doTransferShouldMoveMoneyBetweenAccounts() {
        InMemoryCardRepository repository = new InMemoryCardRepository();
        String fromCard = "4000001234567890";
        String toCard = "4000001234567899";
        repository.saveCard(fromCard, PinHasher.hash("1111"));
        repository.saveCard(toCard, PinHasher.hash("2222"));
        repository.addIncome("1000", fromCard);

        AccountService service = new AccountServiceImpl(repository, new Generator());

        assertTrue(service.doTransfer(fromCard, toCard, "600"));
        assertEquals(400, repository.getBalance(fromCard));
        assertEquals(600, repository.getBalance(toCard));
    }

    @Test
    void doTransferShouldThrowWhenBalanceIsInsufficient() {
        InMemoryCardRepository repository = new InMemoryCardRepository();
        String fromCard = "4000001234567890";
        String toCard = "4000001234567899";
        repository.saveCard(fromCard, PinHasher.hash("1111"));
        repository.saveCard(toCard, PinHasher.hash("2222"));
        repository.addIncome("100", fromCard);

        AccountService service = new AccountServiceImpl(repository, new Generator());

        assertThrows(NotEnoughMoneyInAccountException.class,
                () -> service.doTransfer(fromCard, toCard, "600"));
    }

    private static class InMemoryCardRepository implements CardRepository {

        private final Map<String, Account> accounts = new HashMap<>();

        @Override
        public void saveCard(String numberCard, String pinHash) {
            accounts.put(numberCard, new Account(numberCard, pinHash));
        }

        @Override
        public Account findCard(String numberCard) {
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
            return accounts.remove(cardNumber) != null;
        }

        @Override
        public boolean addIncome(String income, String cardNumber) {Account account = findCard(cardNumber);
            account.setBalance(account.getBalance() + Long.parseLong(income));
            return true;
        }

        @Override
        public long getBalance(String cardNumber) {
            return findCard(cardNumber).getBalance();
        }

        @Override
        public boolean setBalance(String cardNumber, String money) {Account account = findCard(cardNumber);
            long amount = Long.parseLong(money);
            if (account.getBalance() < amount) {
                return false;
            }
            account.setBalance(account.getBalance() - amount);
            return true;
        }

        @Override
        public boolean transferMoney(String fromCardNumber, String toCardNumber, long amount) {
            Account from = findCard(fromCardNumber);
            Account to = findCard(toCardNumber);
            if (from.getBalance() < amount) {
                return false;
            }
            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);
            return true;
        }
    }
}