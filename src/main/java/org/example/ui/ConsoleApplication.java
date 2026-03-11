package org.example.ui;

import org.example.controller.AccountController;
import org.example.except.NumberCardIsNotInDBException;

import java.util.Scanner;

public class ConsoleApplication {
    private final AccountController controller;
    private final Scanner input;

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
                2. Log out
                0. Exit
                """);
    }

    private void createAccount() {
        System.out.println(controller.createAccount());
    }

    private void login() {
        try {
            System.out.println("Enter your card number:");
            String cardNumber = scanToken();
            System.out.println("Enter your PIN:");
            String pin = scanToken();

            boolean accountExists = controller.isPinValid(cardNumber, pin);
            if (!accountExists) {
                System.out.println("\nWrong card number or PIN!\n");
                return;
            }

            System.out.println("\nYou have successfully logged in!\n");
            runAccountMenu(cardNumber);
        } catch (NumberCardIsNotInDBException e) {
            System.out.println("\nWrong card number or PIN!\n");
        }
    }

    private void runAccountMenu(String cardNumber) {
        boolean exit = false;
        while (!exit) {
            printAccountMenu();
            switch (scanInt()) {
                case 1 -> System.out.println(controller.getBalance(cardNumber));
                case 2 -> {
                    System.out.println("\nYou have successfully logged out!\n");
                    exit = true;
                }
                case 0 -> {
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
