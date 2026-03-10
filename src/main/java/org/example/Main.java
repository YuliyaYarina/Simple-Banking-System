package org.example;

import org.example.controller.AccountController;
import org.example.except.NumberCardIsNotInDBException;

import java.util.Scanner;

/**
 * Консольная точка входа в приложение и обработчик пользовательского меню.
 */
public class Main {

    private static final AccountController controller = new AccountController();
    private static final Scanner input = new Scanner(System.in);

    static void main() {
        selectAction();
    }

    /**
     * Печатает главное меню приложения.
     */
    static void printTheMenu() {
        System.out.println("""
                
                1. Create an account
                2. Log into account
                0. Exit
                """);
    }

    /**
     * Печатает меню после успешного входа в аккаунт.
     */
    static void printTheMenuIntoAccount() {
        System.out.println("""
                
                1. Balance
                2. Log out
                0. Exit
                """);
    }

    static void printTheMenuBye() {
        System.out.println("\nBye!");
    }

    /**
     * Главный цикл приложения. Читает действие пользователя и выполняет соответствующую операцию.
     */
    static void selectAction() {
        boolean exit = false;
        while (!exit) {
            printTheMenu();
            switch (scanInt()) {
                case 1 -> createAnAccount();
                case 2 -> searchAccount();
                case 0 -> {
                    printTheMenuBye();
                    exit = true;
                }
                default -> System.out.println("there is no such option, try again.");
            }
        }
        input.close();
    }

    /**
     * Цикл действий внутри авторизованного аккаунта.
     *
     * @param account номер карты текущего аккаунта
     */
    private static void logIntoAccount(long account) {
        boolean exit = false;
        while (!exit) {
            printTheMenuIntoAccount();
            switch(scanInt()) {
                case 1 -> System.out.println(controller.getBalance(account));
                case 2 -> {
                    System.out.println("\nYou have successfully logged out!");
                    exit = true;
                }
                case 0 -> {
                    printTheMenuBye();
                    System.exit(0);
                }
                default -> System.out.println("there is no such option, try again.");
            }
        }
    }

    /**
     * Считывает целое число из консоли с валидацией.
     */
    private static int scanInt() {
        while (!input.hasNextInt()) {
            input.next();
            System.out.println("Please enter a valid number.");
        }
        return input.nextInt();
    }

    /**
     * Считывает long-число из консоли с валидацией.
     */
    private static long scanLong() {
        while (!input.hasNextLong()) {
            input.next();
            System.out.println("Please enter a valid number.");
        }
        return input.nextLong();
    }

    /**
     * Считывает один текстовый токен из консоли (используется для PIN).
     */
    private static String scanToken() {
        return input.next();
    }

    private static void createAnAccount() {
        System.out.println(controller.createAccount());
    }

    /**
     * Выполняет вход в аккаунт по номеру карты и PIN.
     */
    private static void searchAccount() {
        try {
            System.out.println("Enter your card number:");
            long numberCard = scanLong();
            System.out.println("Enter your PIN:");
            String pinCard = scanToken();

            boolean accountExists = controller.equalsPIN(numberCard, pinCard);

            if (!accountExists) {
                System.out.println("\nWrong card number or PIN!\n");
            } else {
                System.out.println("\nYou have successfully logged in!\n");
                logIntoAccount(numberCard);
            }
        } catch (NumberCardIsNotInDBException e) {
            System.out.println("\nWrong card number or PIN!\n");
        }
    }
}
