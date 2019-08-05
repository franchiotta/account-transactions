package org.fchiotta.accounttransactions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.fchiotta.accounttransactions.core.domains.Account;
import org.fchiotta.accounttransactions.core.domains.AccountBalance;

import java.io.IOException;
import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccountStepDefinitions {

    private String name;
    private String lastName;
    private String email;
    private BigDecimal amount;
    private byte[] response;

    private ObjectMapper objectMapper = ObjectMapperConfiguration.createObjectMapper();

    @Given("I have a name {string}, last name {string} and email {string}")
    public void i_have_a_personal_information(String name, String lastName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    @Given("I have {int} dollars in my pocket")
    public void i_got_money(int amount) {
        this.amount = new BigDecimal(amount);
    }

    @When("^I ask for a new account to be created$")
    public void want_to_create_an_account() throws JsonProcessingException {
        AccountBalance balance = new AccountBalance();
        balance.setAmount(amount);

        Account requestBody = new Account(null, name, lastName, email, balance);

        this.response = given().body(objectMapper.writeValueAsBytes(requestBody))
                .port(4567)
                .contentType("application/json")
                .post("/account")
                .asByteArray();
    }

    @Then("^there is a new account created$")
    public void check_if_account_is_created() throws IOException {
        Account account = objectMapper.readValue(this.response, Account.class);

        assertNotNull(account);
        assertNotNull(account.getId());

        byte[] responseBody = given().port(4567)
                .get("/account/" + account.getId())
                .asByteArray();

        Account receivedAccount = objectMapper.readValue(responseBody, Account.class);

        assertEquals(account.getId(), receivedAccount.getId());
        assertEquals(name, receivedAccount.getName());
        assertEquals(lastName, receivedAccount.getLastName());
        assertEquals(email, receivedAccount.getEmail());
        assertEquals(amount, receivedAccount.getBalance().getAmount());
    }
}
