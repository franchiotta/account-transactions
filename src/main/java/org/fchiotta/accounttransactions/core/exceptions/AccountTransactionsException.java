package org.fchiotta.accounttransactions.core.exceptions;

public class AccountTransactionsException extends RuntimeException {
    private int status;

    public AccountTransactionsException(int status, String msg) {
        super(msg);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
