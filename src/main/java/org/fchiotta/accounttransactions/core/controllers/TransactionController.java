package org.fchiotta.accounttransactions.core.controllers;

import org.fchiotta.accounttransactions.core.domains.Account;
import org.fchiotta.accounttransactions.core.domains.Transaction;
import org.fchiotta.accounttransactions.core.exceptions.AccountTransactionsException;
import org.fchiotta.accounttransactions.core.repositories.AccountRepository;
import org.fchiotta.accounttransactions.core.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage the logic related to Transaction entity.
 */
public class TransactionController {

    private static Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public TransactionController(final AccountRepository accountRepository,
                                 final TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Transaction getTransaction(final Long id) {
        Transaction transaction = transactionRepository.getTransaction(id);
        if (transaction == null) {
            throw new AccountTransactionsException(404, String.format("Transaction not found for id %d", id));
        }
        return transaction;
    }

    public Transaction postTransaction(final Transaction transaction) {
        LOG.info(String.format("Requesting transaction between account %d and account %d for $%f with currency %s",
                transaction.getSourceAccount().getId(), transaction.getDestinationAccount().getId(), transaction.getAmount(),
                transaction.getCurrency()));

        if (!"USD".equalsIgnoreCase(transaction.getCurrency())) {
            throw new AccountTransactionsException(400, "Only USD currency is supported at the moment");
        }

        if (transaction.getIdempotenceKey() != null) {
            Transaction storedTransaction = transactionRepository.getTransactionByIdempotenceKey(transaction.getIdempotenceKey(),
                    transaction.getSourceAccount().getId(), transaction.getDestinationAccount().getId());

            if (storedTransaction != null) {
                // Transaction already created, should be idempotent.
                return storedTransaction;
            }
        }

        Account sourceAccount = accountRepository.getAccount(transaction.getSourceAccount().getId());
        if (sourceAccount == null) {
            throw new AccountTransactionsException(400, String.format("Source account %d doesn't exist", transaction.getSourceAccount().getId()));
        }

        Account destinationAccount = accountRepository.getAccount(transaction.getDestinationAccount().getId());
        if (destinationAccount == null) {
            throw new AccountTransactionsException(400, String.format("Destination account %d doesn't exist", transaction.getDestinationAccount().getId()));
        }

        if (sourceAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0) {
            throw new AccountTransactionsException(400, String.format("Source account %d doesn't have enough money", sourceAccount.getId()));
        }

        Transaction newTransaction = transactionRepository.createTransaction(transaction);

        LOG.info("Transaction successfully created with id " + newTransaction.getId());

        return newTransaction;
    }
}
