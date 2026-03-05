package org.example.repository;
import org.example.except.NumberCardIsNotInDBException;
import org.example.model.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicReference;

public class CardRepositoryImpl implements CardRepository {

    private static final String URL = "jdbc:sqlite:C:\\Users\\Yuliya\\bankingCards.db"; // написание теста чо она действительно создается
    private static final SQLiteDataSource dataSource = new SQLiteDataSource();


    // нужен метод по проверке соответствия номеров карты
    //закомментировать тнтерфейс или данный клас
    // тесты

    {
        dataSource.setUrl(URL);
        try (Connection con = dataSource.getConnection()){
            try (Statement statement = con.createStatement()){
                int i = statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                        "id INTEGER PRIMARY KEY," +
                        "number TEXT NOT NULL," +
                        "pin TEXT NOT NULL," +
                        "balance INTEGER DEFAULT 0);");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод добавляет в БД запись об account.
     * @param numberCard номер карты.
     * @param pin PIN карты.
     */
    @Override
    public void updatedCard(String numberCard, String pin) throws SQLException{
        dataSource.setUrl(URL);
        Connection con = dataSource.getConnection();
            try (Statement statement = con.createStatement()){
                statement.executeUpdate("INSERT INTO card(number, pin) " +
                        "VALUES ('" + numberCard + "', '" + pin + "')");
            } catch (SQLException e) {
                throw new SQLException(e);
            }
    }

    /**
     * Метод ищет в БД аккаунт по номеру карты и берет его из БД данные аккаунта без pin, и возвращает.
     * @param numberCard номер карты
     * @return строку с данными аккаунта из БД.
     */
    @Override
    public Account getCard(long numberCard) {
        dataSource.setUrl(URL);
        Account account;
        try(Connection con = dataSource.getConnection()){
            try (Statement statement = con.createStatement()){
                try (ResultSet accounts = statement.executeQuery("SELECT " +
                        "id, " +
                        "number, " +
                        "pin, " +
                        "balance " +
                        "FROM card " +
                        "WHERE number = '" + numberCard + "';")) {

                    long id = accounts.getInt("id");

                    long number = Long.parseLong(accounts.getString("number"));
                    int pin = Integer.parseInt(accounts.getString("pin"));
                    long balance = Long.parseLong(accounts.getString("balance"));

                    account = new Account(number, pin);
                    account.setBalance(balance);
                }
            }
        } catch (NumberCardIsNotInDBException e) {
            throw new NumberCardIsNotInDBException("\n Номера карты: " + numberCard + " нет в BD ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return account;
    }


    /**
     * Метод берет из БД данные аккаунтов без pin, и возвращает.
     * @return строку со всеми аккаунтами из БД.
     */
    @Override
    public String getAccounts() {
        dataSource.setUrl(URL);
        AtomicReference<StringBuilder> accountsBuilder = new AtomicReference<>(new StringBuilder());

        try(Connection con = dataSource.getConnection()){
            try (Statement statement = con.createStatement()){
                try (ResultSet accounts = statement.executeQuery("SELECT " +
                        "id, " +
                        "number, " +
                        "pin, " +
                        "balance " +
                        "FROM card")) {
                    long maxNumberCard = 0;
                    while (accounts.next()) {
                    // Retrieve column values
                    int id = accounts.getInt("id");
                    String number = accounts.getString("number");
                    maxNumberCard = Math.max(maxNumberCard, Long.parseLong(number));
                    String balance = accounts.getString("balance");

                        accountsBuilder.get().append("ID ").append(id).append("\n");
                        accountsBuilder.get().append("\tNumber: ").append(number).append("\n");
                        accountsBuilder.get().append("\tBalance: ").append(balance).append("\n");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accountsBuilder.get().toString();
    }

    /**
     * Находит крайне созданный номер карты.
     * @return возвращает крайне созданный номер карты.
     */
    @Override
    public String searchMaxNumberCard(){
        dataSource.setUrl(URL);
        long maxNumberCard = 0;
        try(Connection con = dataSource.getConnection()){
            try (Statement statement = con.createStatement()){
                try (ResultSet accounts = statement.executeQuery("SELECT " +
                        "number " +
                        "FROM card")) {
                    while (accounts.next()) {
                        long number = accounts.getLong("number");
                        maxNumberCard = Math.max(maxNumberCard, number);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(maxNumberCard).equals("0") ? null : String.valueOf(maxNumberCard) ;
    }
}
