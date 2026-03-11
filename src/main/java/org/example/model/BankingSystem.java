package org.example.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @deprecated В stage 2 используем БД как единственный источник данных.
 * Этот класс изолирован от runtime-потока и оставлен только для обратной совместимости.
 */
@Deprecated
public final class BankingSystem {

    private static final BankingSystem INSTANCE = new BankingSystem();
    private static final Map<String, Account> accounts = new ConcurrentHashMap<>();

    private BankingSystem() {
    }

    public static BankingSystem getInstance() {
        return INSTANCE;
    }

    /**
     * Возвращает все банковские счета.
     * @return банковские счета
     */
    public static Map<String, Account> getAccounts(){
        return accounts;
    }

    /**
     * Находит и возвращает банковский счет.
     * @param numberAccount номер банковского счета.
     * @return банковский счет.
     */
    public static Account getAccount(String numberAccount) {
        return accounts.get(numberAccount);
    }

    /**
     * Добавляет в коллекцию банковский счет.
     *
     * @param numberAccount номер банковского счета.
     * @param account       банковский счет.
     */
    public static void addAccount(String numberAccount, Account account){
        accounts.put(numberAccount, account);
    }

    /**
     * Проверяет на наличие и удаляет из коллекции банковский счет.
     * @param numberAccount номер банковского счета.
     * @return true в случае если объект найден и удален.
     */
    public static boolean removeAccount(String numberAccount){
        return accounts.remove(numberAccount) != null;
    }
}
