package org.fchiotta.accounttransactions.core.repositories;

import org.fchiotta.accounttransactions.core.domains.Transaction;

/**
 * Interface for manipulation of Transaction information against an storage.
 */
public interface TransactionRepository {

    /**
     * Lookups a transaction entity in the underlying repository.
     * @param id transaction id
     * @return a stored transaction with the provided ID.
     */
    Transaction getTransaction(Long id);

    /**
     * Lookups a transaction entity in the underlying repository.
     * @param key idempotence key used when stored
     * @param sourceAccountId source account involved in the transaction
     * @param destinationAccountId destination account involved in the transaction
     * @return a stored transaction with the provided idempotence key.
     */
    Transaction getTransactionByIdempotenceKey(Long key, Long sourceAccountId, Long destinationAccountId);

    /**
     * Stores a new transaction in the underlying repository
     * @param transaction information about the transaction
     * @return the transaction recently stored
     */
    Transaction createTransaction(final Transaction transaction);
}
