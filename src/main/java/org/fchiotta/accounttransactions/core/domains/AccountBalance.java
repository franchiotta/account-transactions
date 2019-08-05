package org.fchiotta.accounttransactions.core.domains;

import java.math.BigDecimal;

public class AccountBalance {
    private BigDecimal amount;
    private String currency;

    public AccountBalance() {}

    public AccountBalance(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
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
}
