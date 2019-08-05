package org.fchiotta.accounttransactions.repository;

import org.fchiotta.accounttransactions.core.domains.AccountBalance;
import org.fchiotta.accounttransactions.core.domains.Account;
import org.fchiotta.accounttransactions.core.domains.Transaction;
import org.fchiotta.accounttransactions.core.repositories.TransactionRepository;
import org.fchiotta.accounttransactions.repository.domains.AccountTable;
import org.fchiotta.accounttransactions.repository.domains.TransactionAccountTable;
import org.fchiotta.accounttransactions.repository.domains.TransactionTable;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Transaction Repository implementation using an in-memory database.
 */
public class InMemoryTransactionRepository implements TransactionRepository {

    @Override
    public Transaction getTransaction(Long id) {
        EntityManager entityManager = HibernateUtil.getEntityManager();

        TransactionTable transaction = entityManager.find(TransactionTable.class, id);
        if (transaction == null) {
            return null;
        }

        return buildTransaction(transaction);
    }

    @Override
    public Transaction getTransactionByIdempotenceKey(Long key, Long sourceAccountId, Long destinationAccountId) {
        EntityManager entityManager = HibernateUtil.getEntityManager();

        TransactionTable transaction = entityManager.createNamedQuery("Transaction.findByIdempotenceKey", TransactionTable.class)
                .setParameter("key", key)
                .setParameter("sourceAccountId", sourceAccountId)
                .setParameter("destinationAccountId", destinationAccountId)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (transaction == null) {
            return null;
        }

        return buildTransaction(transaction);
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        EntityManager entityManager = HibernateUtil.getEntityManager();

        TransactionTable transactionTable = buildTransactionTable(entityManager, transaction);

        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(transactionTable.getSourceAccount().getAccount());
            entityManager.persist(transactionTable.getDestinationAccount().getAccount());
            entityManager.persist(transactionTable.getSourceAccount());
            entityManager.persist(transactionTable.getDestinationAccount());
            entityManager.persist(transactionTable);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }

        return buildTransaction(transactionTable);
    }

    private Transaction buildTransaction(final TransactionTable transactionTable) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionTable.getId());
        transaction.setSourceAccount(buildAccount(transactionTable.getSourceAccount()));
        transaction.setDestinationAccount(buildAccount(transactionTable.getDestinationAccount()));
        transaction.setCurrency(transactionTable.getCurrency());
        transaction.setAmount(transactionTable.getAmount());
        transaction.setIdempotenceKey(transactionTable.getIdempotenceKey());
        transaction.setDateCreated(transactionTable.getDateCreated());
        return transaction;
    }

    private Account buildAccount(final TransactionAccountTable transactionAccount) {
        AccountTable account = transactionAccount.getAccount();
        AccountBalance accountBalance = new AccountBalance(transactionAccount.getBalance(), transactionAccount.getCurrency());
        return new Account(account.getId(), account.getName(), account.getLastName(), account.getEmail(), accountBalance);
    }

    private TransactionTable buildTransactionTable(final EntityManager entityManager, final Transaction transaction) {
        final AccountTable sourceAccount = entityManager.find(AccountTable.class, transaction.getSourceAccount().getId());
        sourceAccount.debit(transaction.getAmount());

        final AccountTable destinationAccount = entityManager.find(AccountTable.class, transaction.getDestinationAccount().getId());
        destinationAccount.credit(transaction.getAmount());

        final TransactionAccountTable transactionSourceAccount = new TransactionAccountTable(sourceAccount, sourceAccount.getBalance().getAmount());
        final TransactionAccountTable transactionDestinationAccount = new TransactionAccountTable(destinationAccount, destinationAccount.getBalance().getAmount());

        return new TransactionTable(transactionSourceAccount, transactionDestinationAccount, transaction.getAmount(), transaction.getCurrency(),
                transaction.getIdempotenceKey());
    }
}
