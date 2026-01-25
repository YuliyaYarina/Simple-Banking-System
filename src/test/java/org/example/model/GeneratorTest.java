package org.example.model;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneratorTest {

    private final String number;

    {
        number = "400000844943340";
    }

    private final LuhnAlgorithm luhnAlgorithm = new  Generator();

    @org.junit.jupiter.api.Test
    void numberCard() {
    }

    @org.junit.jupiter.api.Test
    void PINCard() {
    }

    @org.junit.jupiter.api.Test
    void generatedLuhnAlgorithm() {
        String number = "400000844943340";
        assertEquals("4000008449433403", luhnAlgorithm.generatedLuhnAlgorithm(number));
    }

    @org.junit.jupiter.api.Test
    void addAllNumbers() {
        int[] number = new int[] {8,0,0,0,0,0,7,4,8,9,8,3,6,4,0};
        int expected = 3;
        int actual = luhnAlgorithm.addAllNumbers(number);

        assertEquals(expected,actual);

    }

    @org.junit.jupiter.api.Test
    void subtractNumbersOver9() {
        int[] number = new int[] {8,0,0,0,0,0,16,4,8,9,8,3,6,4,0};
        int[] expected = new int[] {8,0,0,0,0,0,7,4,8,9,8,3,6,4,0};

        assertEquals(Arrays.toString(expected), Arrays.toString(luhnAlgorithm.subtractNumbersOver9(number)));
    }

    @org.junit.jupiter.api.Test
    void multipedOddDigitsBy2() {
        int[] expected = new int[] {8,0,0,0,0,0,16,4,8,9,8,3,6,4,0};

        assertEquals(Arrays.toString(expected), Arrays.toString(luhnAlgorithm.multipedOddDigitsBy2(number.split(""))));
    }
}