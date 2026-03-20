package org.example.service.serviceImpl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LuhnAlgorithmServiceImplTest {

    private final LuhnAlgorithmServiceImpl service = new LuhnAlgorithmServiceImpl();

    @Test
    void generateLuhnNumberShouldReturnValidKnownNumber() {
        assertEquals("4000001234567899", service.generateLuhnNumber("400000123456789"));
    }

    @Test
    void discardsLastDigitOfCardNumberShouldRemoveTailDigit() {
        assertEquals("400000123456789", service.discardsLastDigitOfCardNumber("4000001234567899"));
    }

    @Test
    void multiplyOddDigitsByTwoShouldTransformOddIndexesFromLeft() {
        int[] actual = service.multiplyOddDigitsByTwo(new String[]{"4", "0", "0", "0"});
        assertArrayEquals(new int[]{8, 0, 0, 0}, actual);
    }

    @Test
    void subtractNumbersOver9ShouldNormalizeDigits() {
        int[] actual = service.subtractNumbersOver9(new int[]{12, 9, 18});
        assertArrayEquals(new int[]{3, 9, 9}, actual);
    }

    @Test
    void calculateCheckDigitShouldReturnOneForKnownPrefix() {
        String prefix = "400000123456789";
        int[] multiplied = service.multiplyOddDigitsByTwo(prefix.split(""));
        int[] normalized = service.subtractNumbersOver9(multiplied);

        assertEquals(9, service.calculateCheckDigit(normalized));
    }
}