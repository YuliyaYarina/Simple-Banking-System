package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneratorTest {

    private static final String LUHN_BASE_NUMBER = "400000844943340";

    private final LuhnAlgorithm luhnAlgorithm = new Generator();

    @Test
    void generateCardNumberShouldProduce16DigitNumber() {
        Generator generator = new Generator();

        String cardNumber = generator.generateCardNumber();

        assertTrue(cardNumber.matches("\\d{16}"));
    }

    @Test
    void generatePinShouldBeFourDigitsIncludingLeadingZeros() {
        Generator generator = new Generator();

        for (int i = 0; i < 100; i++) {
            String pin = generator.generatePin();
            assertTrue(pin.matches("\\d{4}"), "PIN must contain exactly 4 digits");
            int value = Integer.parseInt(pin);
            assertTrue(value >= 0 && value <= 9999, "PIN value must be in range 0000..9999");
        }
    }

    @Test
    void generateLuhnNumber() {
        assertEquals("4000008449433403", luhnAlgorithm.generateLuhnNumber(LUHN_BASE_NUMBER));
    }

    @Test
     void calculateCheckDigit() {
        int[] number = new int[]{8, 0, 0, 0, 0, 0, 7, 4, 8, 9, 8, 3, 6, 4, 0};
        int actual = luhnAlgorithm.calculateCheckDigit(number);

        assertEquals(3, actual);
    }

    @Test
    void subtractNumbersOver9() {
        //Given
        int[] number = new int[]{8, 0, 0, 0, 0, 0, 16, 4, 8, 9, 8, 3, 6, 4, 0};

        //While
        String actual = Arrays.toString(luhnAlgorithm.subtractNumbersOver9(number));
        String expected = Arrays.toString(new int[]{8, 0, 0, 0, 0, 0, 7, 4, 8, 9, 8, 3, 6, 4, 0});

        //Then
        assertEquals(expected, actual);
    }

    @Test
    void multiplyOddDigitsByTwo() {
        int[] expected = new int[]{8, 0, 0, 0, 0, 0, 16, 4, 8, 9, 8, 3, 6, 4, 0};

        assertEquals(Arrays.toString(expected), Arrays.toString(luhnAlgorithm.multiplyOddDigitsByTwo(LUHN_BASE_NUMBER.split(""))));
    }
}