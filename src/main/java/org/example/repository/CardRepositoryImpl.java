package org.example.repository;

import org.example.except.NumberCardIsNotInDBException;
import org.example.model.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.*;

/**
 * JDBC-реализация репозитория карт.
 */
public class CardRepositoryImpl implements CardRepository {

    private static final String DEFAULT_DB_NAME = "-fileName.db";
    private static final String DB_PATH = System.getProperty("bank.db.path", DEFAULT_DB_NAME);
    private static final String URL = "jdbc:sqlite:" + DB_PATH;
    private static final SQLiteDataSource dataSource = new SQLiteDataSource();

    static {
        dataSource.setUrl(URL);
    }

    /**
     * Инициализирует источник данных и гарантирует наличие таблицы `card`.
     */
    public CardRepositoryImpl() {
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card ("
                    + "id INTEGER PRIMARY KEY,"
                    + "number TEXT NOT NULL UNIQUE,"
                    + "pin TEXT NOT NULL,"
                    + "balance INTEGER DEFAULT 0);");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }

    /**
     * Сохраняет новую карту в базе данных.
     */
    @Override
    public void saveCard(String numberCard, String pinHash) {
        String insertSql = "INSERT INTO card(number, pin) VALUES (?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(insertSql)) {
            statement.setString(1, numberCard);
            statement.setString(2, pinHash);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create card", e);
        }
    }

    /**
     * Возвращает аккаунт по номеру карты или бросает исключение, если запись не найдена.
     */
    @Override
    public Account findCard(String numberCard) {
        String selectSql = "SELECT number, pin, balance FROM card WHERE number = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(selectSql)) {

            statement.setString(1, numberCard);

            try (ResultSet accounts = statement.executeQuery()) {
                if (!accounts.next()) {
                    throw new NumberCardIsNotInDBException("Card is not found");
                }

                String number = accounts.getString("number");
                String pin = accounts.getString("pin");
                long balance = accounts.getLong("balance");

                Account account = new Account(number, pin);
                account.setBalance(balance);
                return account;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read card", e);
        }
    }

        /**
     * Возвращает форматированный список всех аккаунтов.
     */
    @Override
    public String getAccounts() {
        StringBuilder accountsBuilder = new StringBuilder();

        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement();
             ResultSet accounts = statement.executeQuery("SELECT id, number, balance FROM card")) {

            while (accounts.next()) {
                int id = accounts.getInt("id");
                String number = accounts.getString("number");
                long balance = accounts.getLong("balance");

                accountsBuilder.append("ID ").append(id).append("\n");
                accountsBuilder.append("\tNumber: ").append(number).append("\n");
                accountsBuilder.append("\tBalance: ").append(balance).append("\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read accounts", e);
        }
        return accountsBuilder.toString();
    }

    /**
     * Возвращает максимальный номер карты, хранящийся в таблице.
     */
    @Override
    public String findMaxCardNumber() {
        String maxSql = "SELECT MAX(number) AS max_number FROM card";
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement();
             ResultSet accounts = statement.executeQuery(maxSql)) {

            String maxNumber = accounts.getString("max_number");
            return maxNumber == null ? null : maxNumber;
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get max card number", e);
        }
    }
}