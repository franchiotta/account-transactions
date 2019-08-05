package org.fchiotta.accounttransactions.core.repositories;

import org.fchiotta.accounttransactions.core.domains.Account;

/**
 * Interface for manipulation of Account information against an storage.
 */
public interface AccountRepository {
    /**
     * Lookups an account entity in the underlying repository.
     * @param id account id
     * @return a stored account with the provided ID.
     */
    Account getAccount(Long id);

    /**
     * Stores a new account in the underlying repository
     * @param account information about the acount
     * @return the account recently stored
     */
    Account createAccount(final Account account);
}
