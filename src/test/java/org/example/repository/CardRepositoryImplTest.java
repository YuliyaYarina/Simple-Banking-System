package org.example.repository;

import org.example.except.RecipientCardNumberNotExistException;
import org.example.model.Account;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardRepositoryImplTest {
    private static final String TEST_DB_PATH = "target/test-bankingCards.db";

    @BeforeAll
    static void setupDbPath() {
        System.setProperty("bank.db.path", TEST_DB_PATH);
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
    void findMaxCardNumberShouldReturnMaxInsertedNumber() {
        CardRepository repository = new CardRepositoryImpl();
        long seed = System.nanoTime() % 100_000_000L;
        String low = String.valueOf(4999990000000000L + seed);
        String high = String.valueOf(4999990000000000L + seed + 1);

        repository.saveCard(low, "h1");
        repository.saveCard(high, "h2");

        assertEquals(high, repository.findMaxCardNumber());
    }

    @Test
    void deleteAccountShouldDeletedAccountAndReturnBoolean() {
        CardRepository repository = new CardRepositoryImpl();
        String cardNumber = String.valueOf(4000000000000000L + (System.nanoTime() % 1_000_000_000L));
        String pinHash = "testSalt:testHash";
        repository.saveCard(cardNumber, pinHash);

        boolean result = repository.deleteAccount(cardNumber);

        assertTrue(result);
        assertThrows(RecipientCardNumberNotExistException.class, ()  -> {
            repository.findCard(cardNumber);
        });
    }
}