package org.fchiotta.accounttransactions.repository;

import org.fchiotta.accounttransactions.core.domains.AccountBalance;
import org.fchiotta.accounttransactions.core.repositories.AccountRepository;
import org.fchiotta.accounttransactions.core.domains.Account;
import org.fchiotta.accounttransactions.repository.domains.AccountTable;
import org.fchiotta.accounttransactions.repository.domains.AccountBalanceTable;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Account Repository implementation using an in-memory database.
 */
public class InMemoryAccountRepository implements AccountRepository {

    @Override
    public Account getAccount(Long id) {
        EntityManager entityManager = HibernateUtil.getEntityManager();

        AccountTable account = entityManager.find(AccountTable.class, id);
        if (account == null) {
            return null;
        }

        return buildAccount(account);
    }

    @Override
    public Account createAccount(final Account account) {
        EntityManager entityManager = HibernateUtil.getEntityManager();

        AccountTable accountTable = buildAccountTable(account);

        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(accountTable.getBalance());
            entityManager.persist(accountTable);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }

        account.setId(accountTable.getId());
        return account;
    }

    private Account buildAccount(final AccountTable account) {
        AccountBalance accountBalance = new AccountBalance(account.getBalance().getAmount(), account.getBalance().getCurrency());
        return new Account(account.getId(), account.getName(), account.getLastName(), account.getEmail(), accountBalance);
    }

    private AccountTable buildAccountTable(final Account account) {
        AccountBalanceTable accountBalanceTable = new AccountBalanceTable(account.getBalance().getAmount());
        return new AccountTable(account.getName(), account.getLastName(), account.getEmail(), accountBalanceTable);
    }
}
