package org.fchiotta.accounttransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.fchiotta.accounttransactions.core.controllers.AccountController;
import org.fchiotta.accounttransactions.core.controllers.TransactionController;
import org.fchiotta.accounttransactions.core.domains.Account;
import org.fchiotta.accounttransactions.core.domains.Transaction;
import org.fchiotta.accounttransactions.core.exceptions.AccountTransactionsException;
import org.fchiotta.accounttransactions.core.repositories.AccountRepository;
import org.fchiotta.accounttransactions.core.repositories.TransactionRepository;
import org.fchiotta.accounttransactions.repository.InMemoryAccountRepository;
import org.fchiotta.accounttransactions.repository.InMemoryTransactionRepository;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static spark.Spark.*;

/**
 * Main class for the application.
 * It defines the routing for the different API resources.
 */
public class AccountTransactionsApplication {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setPropertyNamingStrategy(SNAKE_CASE);
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static void main(String[] args) {
        get("/account/:id", (req, res) -> {
            res.type("application/json");
            Long id = Long.valueOf(req.params(":id"));
            AccountRepository repository = new InMemoryAccountRepository();
            AccountController manager = new AccountController(repository);
            return objectMapper.writeValueAsString(manager.getAccount(id));
        });

        post("/account", (req, res) -> {
            res.type("application/json");
            Account account = objectMapper.readValue(req.bodyAsBytes(), Account.class);
            AccountRepository repository = new InMemoryAccountRepository();
            AccountController manager = new AccountController(repository);
            return objectMapper.writeValueAsString(manager.postAccount(account));
        });

        get("/transaction/:id", (req, res) -> {
            res.type("application/json");
            Long id = Long.valueOf(req.params(":id"));
            AccountRepository accountRepository = new InMemoryAccountRepository();
            TransactionRepository transactionRepository = new InMemoryTransactionRepository();
            TransactionController manager = new TransactionController(accountRepository, transactionRepository);
            return objectMapper.writeValueAsString(manager.getTransaction(id));
        });

        post("/transaction", (req, res) -> {
            res.type("application/json");
            Transaction transaction = objectMapper.readValue(req.bodyAsBytes(), Transaction.class);
            AccountRepository accountRepository = new InMemoryAccountRepository();
            TransactionRepository transactionRepository = new InMemoryTransactionRepository();
            TransactionController manager = new TransactionController(accountRepository, transactionRepository);
            return objectMapper.writeValueAsString(manager.postTransaction(transaction));
        });

        exception(AccountTransactionsException.class, (ex, req, res) -> {
            res.type("application/json");
            res.status(ex.getStatus());
            res.body(String.format("{\"message\":\"%s\", \"status\":%d}", ex.getMessage(), ex.getStatus()));
        });

        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Resource not found\", \"status\":404}";
        });

        internalServerError((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"An error ocurred in the server\", \"status\":500}";
        });
    }
}
