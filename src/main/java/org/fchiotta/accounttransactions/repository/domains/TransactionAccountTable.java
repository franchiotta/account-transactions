package org.fchiotta.accounttransactions.repository.domains;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "TransactionAccount")
@Table(name = "TRANSACTION_ACCOUNT")
public class TransactionAccountTable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private AccountTable account;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @Column(name = "VERSION")
    @Version
    private Long version;

    public TransactionAccountTable() {
    }

    public TransactionAccountTable(AccountTable account, BigDecimal balance) {
        this.account = account;
        this.currency = "USD";
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountTable getAccount() {
        return account;
    }

    public void setAccount(AccountTable account) {
        this.account = account;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
