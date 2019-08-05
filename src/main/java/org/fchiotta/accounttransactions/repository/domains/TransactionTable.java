package org.fchiotta.accounttransactions.repository.domains;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity(name = "Transaction")
@Table(name = "TRANSACTION")
@NamedQuery(name = "Transaction.findByIdempotenceKey",
        query = "Select t From Transaction t Where t.idempotenceKey=:key and sourceAccount.account.id=:sourceAccountId " +
                "and destinationAccount.account.id=:destinationAccountId")
public class TransactionTable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRANSACTION_SOURCE_ACCOUNT_ID")
    private TransactionAccountTable sourceAccount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRANSACTION_DESTINATION_ACCOUNT_ID")
    private TransactionAccountTable destinationAccount;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "IDEMPOTENCE_KEY")
    private Long idempotenceKey;

    @Column(name = "DATE_CREATED")
    private Instant dateCreated;

    @Column(name = "VERSION")
    @Version
    private Long version;

    public TransactionTable() {
    }

    public TransactionTable(TransactionAccountTable sourceAccount, TransactionAccountTable destinationAccount, BigDecimal amount, String currency, Long idempotenceKey) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.currency = currency;
        this.idempotenceKey = idempotenceKey;
        this.dateCreated = Instant.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionAccountTable getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(TransactionAccountTable sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public TransactionAccountTable getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(TransactionAccountTable destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
