package org.example.repository;

import org.example.except.RecipientCardNumberNotExistException;
import org.example.model.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.*;

/**
 * JDBC-реализация репозитория карт.
 */
public class CardRepositoryImpl implements CardRepository {

    private static final String DEFAULT_DB_NAME = "-fileName.db";
    private final SQLiteDataSource dataSource;

    /**
     * Инициализирует источник данных и гарантирует наличие таблицы `card`.
     */
    public CardRepositoryImpl() {
        String dbPath = System.getProperty("bank.db.path", DEFAULT_DB_NAME);
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + dbPath);

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

    @Override
    public Account findCard(String numberCard) {
        String selectSql = "SELECT number, pin, balance FROM card WHERE number = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(selectSql)) {

            statement.setString(1, numberCard);

            try (ResultSet accounts = statement.executeQuery()) {
                if (!accounts.next()) {
                    throw new RecipientCardNumberNotExistException();
                }

                String number = accounts.getString("number");
                String pin = accounts.getString("pin");
                long balance = accounts.getLong("balance");

                Account account = new Account(number, pin);
                account.setBalance(balance);
                return account;
            }
        } catch (SQLException e) {
            throw new RecipientCardNumberNotExistException();
        }
    }

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

    @Override
    public boolean deleteAccount(String cardNumber) {
        String deleteSql = "DELETE FROM card WHERE number = ?";
        try (Connection con = dataSource.getConnection();
            PreparedStatement statement = con.prepareStatement(deleteSql)) {
            statement.setString(1, cardNumber);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete account", e);
        }
    }

    @Override
    public long getBalance(String cardNumber) {
        String balanceSql = "SELECT balance FROM card WHERE number = ?";
        try (Connection con = dataSource.getConnection();
            PreparedStatement statement = con.prepareStatement(balanceSql)) {
            statement.setString(1, cardNumber);
            try (ResultSet balance = statement.executeQuery()) {
                return balance.getLong("balance");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get balance for card", e);
        }
    }

    @Override
    public boolean setBalance(String cardNumber, String money) {
        String updateSql = "UPDATE card SET balance = balance - ? WHERE number = ? AND balance >= ?";
        try (Connection con = dataSource.getConnection();
            PreparedStatement statement = con.prepareStatement(updateSql)) {
            statement.setLong(1, Long.parseLong(money));
            statement.setString(2, cardNumber);
            statement.setLong(3, Long.parseLong(money));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set balance", e);
        }
    }

    @Override
    public boolean addIncome(String income, String numberCard) {
        String updateSql = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try (Connection con = dataSource.getConnection();
            PreparedStatement statement = con.prepareStatement(updateSql)) {
            statement.setLong(1, Long.parseLong(income));
            statement.setString(2, numberCard);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add income balance", e);
        }
    }

    @Override
    public boolean transferMoney(String fromCardNumber, String toCardNumber, long amount) {
        String withdrawSql = "UPDATE card SET balance = balance - ? WHERE number = ? AND balance >= ?";
        String depositSql = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement withdrawStatement = con.prepareStatement(withdrawSql);
             PreparedStatement depositStatement = con.prepareStatement(depositSql)) {
            con.setAutoCommit(false);

            withdrawStatement.setLong(1, amount);
            withdrawStatement.setString(2, fromCardNumber);
            withdrawStatement.setLong(3, amount);

            if (withdrawStatement.executeUpdate() == 0) {
                con.rollback();
                return false;
            }

            depositStatement.setLong(1, amount);
            depositStatement.setString(2, toCardNumber);
            if (depositStatement.executeUpdate() == 0) {
                con.rollback();
                return false;
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to transfer money", e);
        }
    }
}