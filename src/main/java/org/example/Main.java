package org.example;

import org.example.controller.AccountController;
import org.example.except.NumberCardIsNotInDBException;

import java.util.InputMismatchException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final AccountController controller= new AccountController();
    private static Scanner input;

    {
        input = new Scanner(System.in);
    }


    static void main() {
        selectsAction();
    }

    /**
     * Выводит на экран меню список действий.
     */
    static void printTheMenu() {
        System.out.println("""
                
                1. Create an account
                2. Log into account
                0. Exit
                """);
    }

    /**
     * Выводит на экран меню список действий в аккаунте.
     */
    static void printTheMenuIntoAccount() {
        System.out.println("""
                1. Balance
                2. Log out
                0. Exit
                """);
    }
    /**
     * Выводит на экран меню "Bye!".
     */
    static void printTheMenuBye() {
        System.out.println("\nBye!");
    }

    /**
     * По введенному номеру, выбирает действие.
     */
    static void selectsAction() {
        boolean exit = false;
        while (!exit) {
            printTheMenu();
            switch ((int)scan()) {
                case 1:
                    createAnAccount();
                    break;
                case 2:
                    searchAccount();
                    break;
                case 0:
                    input.close();
                    printTheMenuBye();
                    exit = true;
                    break;
                default:
                    System.out.println("there is no such option, try again.");
            }
        }
    }

    /**
     * По введенному номеру, выбирает действие в аккаунте.
     * @param account аккаунт.
     */
    private static void logIntoAccount(Long account) {
        boolean exit = false;
        while (!exit){
            printTheMenuIntoAccount();
            switch((int) scan()) {
                case 1:
                    System.out.println(controller.getBalance(account));
                    break;
                case 2:
                    System.out.println("\n1You have successfully logged out!");
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("there is no such option, try again.");
            }
        }
    }

    /**
     * Сканирует введенный номер.
     *
     * @return введенный номер.
     */
    private static long scan() {
        try {
            input = new Scanner(System.in);
            return input.nextLong();
        } catch (NullPointerException e) {
            System.out.println("Please enter a text, NullPointerException");
            return scan();
        } catch (InputMismatchException e) {
            System.out.println("Please enter a text again" +
                    "\n Исключение несоответствия входных данных");
            return scan();
        }
    }

    /**
     * Создает аккаунт, выводит на печать.
     */
    private static void createAnAccount(){
        System.out.println(controller.createAccount());
    }

    /**
     * Находит аккаунт.
     */
    private static void searchAccount()  {
        try {
            System.out.println("Enter your card number:");
            long numberCard = scan();
            System.out.println("Enter your PIN:");
            String PINCard = String.valueOf(scan());

            Boolean account = controller.equalsPIN(numberCard, PINCard);

            if (!account) {
                System.out.println("\nWrong card number or PIN!\n");
            } else {
                System.out.println("\nYou have successfully logged in!\n");
                logIntoAccount(numberCard);
            }
        } catch (NumberCardIsNotInDBException e) {
//        throw new NumberCardIsNotInDBException("\n Номера карты: " + " нет в BD ");
            String message = e.getMessage();
            System.out.println(message);
        }
    }
}
