package org.example.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс реализует Singleton, и представляет банковскую систему с учетными записями клиентов.
 */
public class BankingSystem {

    private static BankingSystem instance;

    private static final Map<Long, Account> accounts = new ConcurrentHashMap<>();

    public synchronized BankingSystem getInstance(){
        if(instance == null){
            instance = new BankingSystem();

            return instance;
        } else return instance;
    }

    /**
     * Возвращает все банковские счета.
     * @return банковские счета
     */
    public static Map<Long, Account> getAccounts(){
        return accounts;
    }

    /**
     * Находит и возвращает банковский счет.
     * @param numberAccount номер банковского счета.
     * @return банковский счет.
     */
    public static Account getAccount(Long numberAccount) throws Exception{
        return accounts.get(numberAccount);
    }

    /**
     * Добавляет в коллекцию банковский счет.
     *
     * @param numberAccount номер банковского счета.
     * @param account       банковский счет.
     */
    public static void addAccount(Long numberAccount, Account account){
        accounts.put(numberAccount, account);
    }

    /**
     * Проверяет на наличие и удаляет из коллекции банковский счет.
     * @param numberAccount номер банковского счета.
     * @return true в случае если объект найден и удален.
     */
    public static boolean removeAccount(Long numberAccount){
        if(accounts.containsKey(numberAccount)) {
            return accounts.remove(numberAccount, accounts.get(numberAccount));
        }else
            return false;
    }
}
