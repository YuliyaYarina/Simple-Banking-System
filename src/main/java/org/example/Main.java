package org.example;

import org.example.controller.AccountController;
import org.example.ui.ConsoleApplication;

import java.util.Scanner;

/**
 * Консольная точка входа в приложение и обработчик пользовательского меню.
 */
public class Main {

    static void main(String[] args) {
        AccountController controller = new AccountController();
        try (Scanner scanner = new Scanner(System.in)) {
            ConsoleApplication app = new ConsoleApplication(controller, scanner);
            app.run();
        }
    }
}