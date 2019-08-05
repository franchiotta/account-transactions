package org.fchiotta.accounttransactions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.fchiotta.accounttransactions.core.domains.Account;
import org.fchiotta.accounttransactions.core.domains.AccountBalance;
import org.fchiotta.accounttransactions.core.domains.Transaction;

import java.io.IOException;
import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class TransactionStepDefinitions {

    private Long sourceAccountId;
    private Long destinationAccountId;
    private BigDecimal amount;
    private String currency;
    private Long idempotenceKey;

    private byte[] response;
    private byte[] retriedResponse;

    private ObjectMapper objectMapper = ObjectMapperConfiguration.createObjectMapper();

    @Given("source account with balance {int}")
    public void source_account_with_balance(int amount) throws IOException {

        AccountBalance balance = new AccountBalance();
        balance.setAmount(new BigDecimal(amount));

        Account requestBody = new Account(null, "arya", "stark", "arya.stark@noreply.com", balance);

        byte[] responseBody = given()
                .body(objectMapper.writeValueAsBytes(requestBody))
                .port(4567)
                .contentType("application/json")
                .post("/account")
                .asByteArray();

        Account account = objectMapper.readValue(responseBody, Account.class);

        assertNotNull(account);
        assertNotNull(account.getId());

        this.sourceAccountId = account.getId();
    }

    @Given("destination account with balance {int}")
    public void destination_account_with_balance(int amount) throws IOException {

        AccountBalance balance = new AccountBalance();
        balance.setAmount(new BigDecimal(amount));

        Account requestBody = new Account(null, "tyrion", "lannister", "tyrion.lannister@noreply.com", balance);

        byte[] responseBody = given()
                .body(objectMapper.writeValueAsBytes(requestBody))
                .port(4567)
                .contentType("application/json")
                .post("/account")
                .asByteArray();

        Account account = objectMapper.readValue(responseBody, Account.class);

        assertNotNull(account);
        assertNotNull(account.getId());

        this.destinationAccountId = account.getId();
    }

    @Given("I want to transfer {int} with currency {string}")
    public void with_money_to_transfer(int amount, String currency) {
        this.amount = new BigDecimal(amount);
        this.currency = currency;
    }

    @Given("idempotence key {int}")
    public void with_idempotence_key(int idempotenceKey) {
        this.idempotenceKey = (long) idempotenceKey;
    }

    @When("^I ask for a new transaction to be created$")
    public void ask_for_transaction() throws JsonProcessingException {
        Transaction transaction = createTransaction();
        this.response = postTransaction(transaction);
    }

    @When("^I ask twice for a new transaction to be created$")
    public void ask_twice_for_transaction() throws JsonProcessingException {
        Transaction transaction = createTransaction();
        this.response = postTransaction(transaction);
        this.retriedResponse = postTransaction(transaction);
    }

    @Then("^there is a new transaction created$")
    public void check_if_transaction_is_created() throws IOException {
        Transaction transaction = objectMapper.readValue(response, Transaction.class);

        assertNotNull(transaction);
        assertNotNull(transaction.getId());

        byte[] responseBody = given().port(4567)
                .get("/transaction/" + transaction.getId())
                .asByteArray();

        Transaction retrievedTransaction = objectMapper.readValue(responseBody, Transaction.class);

        assertEquals(transaction.getId(), retrievedTransaction.getId());
        assertEquals(sourceAccountId, retrievedTransaction.getSourceAccount().getId());
        assertEquals(destinationAccountId, retrievedTransaction.getDestinationAccount().getId());
        assertEquals(amount, retrievedTransaction.getAmount());
        assertEquals(currency, retrievedTransaction.getCurrency());
    }

    @Then("^just one transaction is created$")
    public void just_one_transaction_created() throws IOException {
        Transaction transaction = objectMapper.readValue(response, Transaction.class);
        Transaction retriedTransaction = objectMapper.readValue(retriedResponse, Transaction.class);

        assertNotNull(transaction);
        assertNotNull(transaction.getId());
        assertNotNull(retriedTransaction);
        assertNotNull(retriedTransaction.getId());
        assertEquals(transaction.getId(), retriedTransaction.getId());

        byte[] responseBody = given().port(4567)
                .get("/transaction/" + transaction.getId())
                .asByteArray();

        Transaction retrievedTransaction = objectMapper.readValue(responseBody, Transaction.class);

        assertEquals(retriedTransaction.getId(), retrievedTransaction.getId());
        assertEquals(sourceAccountId, retrievedTransaction.getSourceAccount().getId());
        assertEquals(destinationAccountId, retrievedTransaction.getDestinationAccount().getId());
        assertEquals(amount, retrievedTransaction.getAmount());
        assertEquals(currency, retrievedTransaction.getCurrency());
    }

    @Then("ended with an error {int} and contains message {string}")
    public void endeed_with_an_error(int status, String message) throws IOException {
        ErrorMessage errorMessage = objectMapper.readValue(response, ErrorMessage.class);
        assertEquals(status, errorMessage.getStatus());
        assertTrue(errorMessage.getMessage().contains(message));
    }

    private Transaction createTransaction() {
        Account sourceAccount = new Account();
        sourceAccount.setId(sourceAccountId);

        Account destinationAccount = new Account();
        destinationAccount.setId(destinationAccountId);

        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setIdempotenceKey(idempotenceKey);

        return transaction;
    }

    private byte[] postTransaction(Transaction transaction) throws JsonProcessingException {
        return given()
                .body(objectMapper.writeValueAsBytes(transaction))
                .port(4567)
                .contentType("application/json")
                .post("/transaction")
                .asByteArray();
    }

    public static class ErrorMessage {
        private String message;
        private int status;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

}
