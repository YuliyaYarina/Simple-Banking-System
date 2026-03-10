package org.example.repository;

import org.example.except.NumberCardIsNotInDBException;
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
    void updatedCardAndGetCardShouldPersistAndReadAccount() {
        CardRepository repository = new CardRepositoryImpl();
        String cardNumber = String.valueOf(4000000000000000L + (System.nanoTime() % 1_000_000_000L));
        String pin = "0123";

        repository.updatedCard(cardNumber, pin);
        Account account = repository.getCard(Long.parseLong(cardNumber));

        assertEquals(Long.parseLong(cardNumber), account.getCardNumber());
        assertEquals(pin, account.getPin());
        assertEquals(0, account.getBalance());
    }

    @Test
    void getCardShouldThrowWhenCardIsMissing() {
        CardRepository repository = new CardRepositoryImpl();

        assertThrows(NumberCardIsNotInDBException.class, () -> repository.getCard(4999999999999999L));
    }

    @Test
    void searchMaxNumberCardShouldReturnMaxInsertedNumber() {
        CardRepository repository = new CardRepositoryImpl();
        long seed = System.nanoTime() % 100_000_000L;
        String low = String.valueOf(4999990000000000L + seed);
        String high = String.valueOf(4999990000000000L + seed + 1);

        repository.updatedCard(low, "0001");
        repository.updatedCard(high, "0002");

        assertEquals(high, repository.searchMaxNumberCard());
    }
}