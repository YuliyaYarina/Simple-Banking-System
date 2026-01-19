package org.example;

import org.example.controller.AccountController;
import org.example.model.Account;
import org.example.service.AccountService;
import org.example.service.serviceImpl.AccountServiceImpl;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final AccountService service = new AccountServiceImpl();
    private static final AccountController controller= new AccountController();

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
     * По введенному номеру, выбирает действие.
     */
    static void selectsAction() {
        printTheMenu();
        switch (Integer.parseInt(scan())) {
            case 1:
                createAnAccount();
                selectsAction();
                break;
            case 2:

                searchAccount();
                selectsAction();
                break;
            case 0:
                System.out.println(0);
                break;
                default:
                    System.out.println("there is no such option, try again.");
                    selectsAction();
        }
    }

    /**
     * По введенному номеру, выбирает действие в аккаунте.
     * @param account аккаунт.
     */
    private static void logIntoAccount(Account account) {
        printTheMenuIntoAccount();
        switch(Integer.parseInt(scan())) {
            case 1:
                System.out.println(controller.getBalance(account));
                logIntoAccount(account);
                break;
                case 2:
                    System.out.println("\n1You have successfully logged out!");
                    break;
                    case 0:
                        selectsAction();
                        break;
            default:
                System.out.println("there is no such option, try again.");
                selectsAction();
        }

    }

    /**
     * Сканирует введенный номер.
     * @return введенный номер.
     */
    private static String scan() {
        try {
            Scanner input = new Scanner(System.in);
            return input.next();
        } catch (NullPointerException e) {
            System.out.println("Please enter a text, NullPointerException");
            return scan();
        } catch (Exception e) {
            System.out.println("Please enter a text again");
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
                Long numberCard = Long.valueOf(scan());
                System.out.println("Enter your PIN:");
                int PINCard = Integer.parseInt(scan());

                Account account = controller.loginOnNumberCard(numberCard, PINCard);

                if (account == null){
                    System.out.println("\nWrong card number or PIN!\n");

                }else {
                    System.out.println("\nYou have successfully logged in!\n");
                    logIntoAccount(account);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
    }
}
