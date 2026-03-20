package org.example.ui;

import org.example.controller.AccountController;
import org.example.except.RecipientCardNumberNotExistException;

import java.util.Scanner;

public class ConsoleApplication {
    private final AccountController controller;
    private final Scanner input;

    private static final int BALANCE = 1;
    private static final int ADD_INCOME = 2;
    private static final int DO_TRANSFER = 3;
    private static final int CLOSE_ACCOUNT = 4;
    private static final int LOG_OUT = 5;
    private static final int EXIT = 0;

    public ConsoleApplication(AccountController controller, Scanner input) {
        this.controller = controller;
        this.input = input;
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            switch (scanInt()) {
                case 1 -> createAccount();
                case 2 -> login();
                case 0 -> {
                    System.out.println("\nBye!");
                    exit = true;
                }
                default -> System.out.println("there is no such option, try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("""

                1. Create an account
                2. Log into account
                0. Exit
                """);
    }

    private void printAccountMenu() {
        System.out.println("""
                
                1. Balance
                2. Add income
                3. Do transfer
                4. Close account
                5. Log out
                0. Exit
                """);
    }

    private void createAccount() {
        System.out.println(controller.createAccount());
    }

    private void login() {
            System.out.println("Enter your card number:");
            String cardNumber = scanToken();
            System.out.println("Enter your PIN:");
            String pin = scanToken();

        try {
            boolean accountExists = controller.isPinValid(cardNumber, pin);
            if (!accountExists) {
                System.out.println("\nWrong card number or PIN!");
            } else {
                System.out.println("\nYou have successfully logged in!");
                runAccountMenu(cardNumber);
            }
        } catch (RecipientCardNumberNotExistException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addIncome(String cardNumber) {
            System.out.println("\nEnter income:");
            String income = scanToken();

            boolean addIncome = controller.addIncome(income, cardNumber);
            if (addIncome) System.out.println("\nIncome was added!");
    }

    private void doTransfer(String cardNumber) {
            System.out.println("""
                    
                    Transfer
                    Enter card number:""");
        String cardNumberDoTransfer = scanToken();

        boolean checkCardNumber = controller.isCardNumberValid(cardNumber, cardNumberDoTransfer);
        if (checkCardNumber) {
            System.out.println("Enter how much money you want to transfer:");
            String money = scanToken();

            boolean doTransfer =  controller.doTransfer(cardNumber, cardNumberDoTransfer, money);
            if(doTransfer) System.out.println("Success!");
        }
    }

    private void runAccountMenu(String cardNumber) {
        boolean exit = false;
        while (!exit) {
            printAccountMenu();
            switch (scanInt()) {
                case BALANCE -> controller.getBalance(cardNumber);
                case ADD_INCOME -> addIncome(cardNumber);
                case DO_TRANSFER -> doTransfer(cardNumber);
                case CLOSE_ACCOUNT -> {
                    System.out.println(controller.deleteAccount(cardNumber));
                    exit = true;
                }
                case LOG_OUT -> {
                    System.out.println("\nYou have successfully logged out!");
                    exit = true;
                }
                case EXIT -> {
                    System.out.println("\nBye!");
                    System.exit(0);
                }
                default -> System.out.println("there is no such option, try again.");
            }
        }
    }

    private int scanInt() {
        while (!input.hasNextInt()) {
            input.next();
            System.out.println("Please enter a valid number.");
        }
        return input.nextInt();
    }

    private String scanToken() {
        return input.next();
    }
}
