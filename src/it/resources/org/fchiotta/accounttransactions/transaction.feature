Feature: Suite for checking transaction features.

  Scenario: Creating a new transaction
    Given source account with balance 100
    Given destination account with balance 0
    Given I want to transfer 10 with currency "USD"
    When I ask for a new transaction to be created
    Then there is a new transaction created

  Scenario: Should fail when tyring to create a transaction and source account does not have enough money
    Given source account with balance 10
    Given destination account with balance 0
    Given I want to transfer 100 with currency "USD"
    When I ask for a new transaction to be created
    Then ended with an error 400 and contains message "doesn't have enough money"

  Scenario: Should fail when tyring to create a transaction with a currency differente than USD
    Given source account with balance 100
    Given destination account with balance 0
    Given I want to transfer 100 with currency "EUR"
    When I ask for a new transaction to be created
    Then ended with an error 400 and contains message "Only USD currency is supported at the moment"

  Scenario: Should retrieve same transaction when multiple requests are done with same idempotence key
    Given source account with balance 1000
    Given destination account with balance 0
    Given idempotence key 1234
    Given I want to transfer 100 with currency "USD"
    When I ask twice for a new transaction to be created
    Then just one transaction is created


