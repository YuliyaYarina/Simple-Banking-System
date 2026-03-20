package org.example.repository;

import org.example.except.RecipientCardNumberNotExistException;
import org.example.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CardRepositoryImplTest {

    @BeforeEach
     void setupDbPath() throws Exception {
        Path dbFile = Files.createTempFile("test-bankingCards-", ".db");
        System.setProperty("bank.db.path", dbFile.toString());
    }
    @Test
    void saveCardAndFindCardShouldPersistAndReadAccount() {
        CardRepository repository = new CardRepositoryImpl();
        String cardNumber = String.valueOf(4000000000000000L + (System.nanoTime() % 1_000_000_000L));
        String pinHash = "testSalt:testHash";

        repository.saveCard(cardNumber, pinHash);
        Account account = repository.findCard(cardNumber);

        assertEquals(cardNumber, account.getCardNumber());
        assertEquals(pinHash, account.getPin());
        assertEquals(0, account.getBalance());
    }

    @Test
    void findCardShouldThrowWhenCardIsMissing() {
        CardRepository repository = new CardRepositoryImpl();

        assertThrows(RecipientCardNumberNotExistException.class, () -> repository.findCard("4999999999999999"));
    }

    @Test
    void deleteAccountShouldDeleteAccountAndReturnTrue() {
        CardRepository repository = new CardRepositoryImpl();
        String cardNumber = String.valueOf(4000000000000000L + (System.nanoTime() % 1_000_000_000L));
        String pinHash = "testSalt:testHash";
        repository.saveCard(cardNumber, pinHash);

        boolean result = repository.deleteAccount(cardNumber);

        assertTrue(result);
        assertThrows(RecipientCardNumberNotExistException.class, () -> repository.findCard(cardNumber));
    }

    @Test
    void transferMoneyShouldAtomicallyMoveBalance() {
        CardRepository repository = new CardRepositoryImpl();
        String from = "4000001234567890";
        String to = "4000001234567899";

        repository.saveCard(from, "h1");
        repository.saveCard(to, "h2");
        repository.addIncome("800", from);

        assertTrue(repository.transferMoney(from, to, 300));
        assertEquals(500, repository.getBalance(from));
        assertEquals(300, repository.getBalance(to));
    }
}