package org.fchiotta.accounttransactions.core.controllers;

import org.fchiotta.accounttransactions.core.domains.Account;
import org.fchiotta.accounttransactions.core.exceptions.AccountTransactionsException;
import org.fchiotta.accounttransactions.core.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage the logic related to Account entity.
 */
public class AccountController {

    private static Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private AccountRepository accountRepository;

    public AccountController(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(final Long id) {
        Account account = accountRepository.getAccount(id);
        if (account == null) {
            throw new AccountTransactionsException(404, String.format("Account not found for id %d", id));
        }
        return account;
    }

    public Account postAccount(final Account account) {
        Account newAccount = accountRepository.createAccount(account);
        LOG.info("Account successfully created with id " + newAccount.getId());
        return newAccount;
    }
}
