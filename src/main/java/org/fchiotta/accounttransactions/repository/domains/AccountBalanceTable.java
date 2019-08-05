package org.fchiotta.accounttransactions.repository.domains;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
public class AccountBalanceTable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "VERSION")
    @Version
    private Long version;

    public AccountBalanceTable() {
    }

    public AccountBalanceTable(BigDecimal amount) {
        this.amount = amount;
        this.currency = "USD";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
