package org.example.model;

import java.util.Arrays;

import static org.example.model.Generator.incrementAccountIdentifier;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneratorTest {

    private final String number;

    {
        number = "400000844943340";
    }

    private final LuhnAlgorithm luhnAlgorithm = new  Generator();

    @org.junit.jupiter.api.Test
    void numberCard() {
        //Given
        String lastCardNumber = "4000008449433403";
        String n = null;
        //When
        String[] expected = "844943341".split("");
//        String[] actual= incrementAccountIdentifier(lastCardNumber);
        //Then
//        assertEquals(Arrays.toString(actual),Arrays.toString(expected));
//        assertEquals("[8, 4, 4, 9, 4, 3, 3, 4, 2]", Arrays.toString(incrementAccountIdentifier(n)));
    }

    @org.junit.jupiter.api.Test
    void PINCard() {
    }

    @org.junit.jupiter.api.Test
    void generatedLuhnAlgorithm() {
        //Given
        String number = "400000844943340";
        //When
        //Then
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
        //Given
        int[] number = new int[] {8,0,0,0,0,0,16,4,8,9,8,3,6,4,0};

        //While
        String actual = Arrays.toString(luhnAlgorithm.subtractNumbersOver9(number));
        String expected = Arrays.toString(new int[] {8,0,0,0,0,0,7,4,8,9,8,3,6,4,0});

        //Then
        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void multipedOddDigitsBy2() {
        int[] expected = new int[] {8,0,0,0,0,0,16,4,8,9,8,3,6,4,0};

        assertEquals(Arrays.toString(expected), Arrays.toString(luhnAlgorithm.multipedOddDigitsBy2(number.split(""))));
    }
}