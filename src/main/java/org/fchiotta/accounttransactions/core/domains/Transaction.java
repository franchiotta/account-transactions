package org.fchiotta.accounttransactions.core.domains;

import java.math.BigDecimal;
import java.time.Instant;

public class Transaction {
    private Long id;
    private Account sourceAccount;
    private Account destinationAccount;
    private String currency;
    private BigDecimal amount;
    private Long idempotenceKey;
    private Instant dateCreated;

    public Transaction() {}

    public Transaction(Account sourceAccount, Account destinationAccount, String currency,
                       BigDecimal amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.currency = currency;
        this.amount = amount;
    }

    public Transaction(Long id, Account sourceAccount, Account destinationAccount, String currency,
                       BigDecimal amount, Long idempotenceKey, Instant dateCreated) {
        this.id = id;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.currency = currency;
        this.amount = amount;
        this.idempotenceKey = idempotenceKey;
        this.dateCreated = dateCreated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account accountTransaction) {
        this.sourceAccount = accountTransaction;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getIdempotenceKey() {
        return idempotenceKey;
    }

    public void setIdempotenceKey(Long idempotenceKey) {
        this.idempotenceKey = idempotenceKey;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }
}
