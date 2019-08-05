package org.fchiotta.accounttransactions.repository.domains;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "Account")
@Table(name = "ACCOUNT")
public class AccountTable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="BALANCE_ID")
    private AccountBalanceTable balance;

    @Column(name = "VERSION")
    @Version
    private Long version;

    public AccountTable() {
    }

    public AccountTable(String name, String lastName, String email, AccountBalanceTable accountBalanceTable) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.balance = accountBalanceTable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountBalanceTable getBalance() {
        return balance;
    }

    public void setBalance(AccountBalanceTable balance) {
        this.balance = balance;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void credit(BigDecimal amount) {
        BigDecimal currentAmount = this.balance.getAmount();
        this.balance.setAmount(currentAmount.add(amount));
    }

    public void debit(BigDecimal amount) {
        BigDecimal currentAmount = this.balance.getAmount();
        this.balance.setAmount(currentAmount.subtract(amount));
    }
}
