package org.fchiotta.accounttransactions.core.domains;

public class Account {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private AccountBalance balance;

    public Account() {}

    public Account(Long id, String name, String lastName, String email, AccountBalance balance) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.balance = balance;
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

    public AccountBalance getBalance() {
        return balance;
    }

    public void setBalance(AccountBalance balance) {
        this.balance = balance;
    }

}
