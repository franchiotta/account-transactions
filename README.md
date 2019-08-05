# Account Transaction Application

A financial transaction manager that allows to manipulate user balance accounts.

## Getting started

This section will cover usage of the application as well as development details.

### Usage

It manages the concept of accounts and transactions. Accounts are like digital wallet of users and transactions are money transfers between certain accounts. 

#### Creating an account

```
curl -XPOST -s "<host>:<port>/account" \
-d \
'{ 
   "name":"foo,
   "last_name":" bar"",
   "email":"foo.bar@no-reply.com",
   "balance":{"amount":100, "currency":"USD"}
}'
```

The response will provide you with an account id that you can use to retrieve it later:

```
curl -XGET -s <host>:<port>/account/:id
```

#### Creating a transaction

A transaction is composed for a source account, a destination account, an amount and a respective currency.

```
curl -s -XPOST <host>:<port>/transaction \
-d \
'{
	"source_account":{"id":2},
	"destination_account":{"id":4},
	"amount":100,
	"currency":"USD".
	"idempotence_key":7162537612537 
}'
```

Note that idempotence_key attribute is for forcing idempotent behaviour. No matter how many times you post this body, 
It will create just one transaction for same source and destination account.


### Prerequisites

- Java 8 (or higher)
- Maven 3

### Dependencies

- [Spark Framework](http://sparkjava.com/)
- Hibernate
- H2 in memory database
- Cucumber
- Jackson
- Junit
- [Rest-Assured](http://rest-assured.io/)

Optimistic locking feature of hibernate is enabled in order to avoid race conditions on account balances.

### Setup

As this is a maven project, you can generate a package (jar) and run the application as a standalone one:

```
mvn clean package
```
Maven will place the generated package in target folder of the project with name account-transactions.jar by default.
Once generated, it is possible to execute it using java command (it is a bundle with all dependencies).

```
java -jar account-transactions.jar
```

Otherwise, you can simply execute a maven goal to run the application:

```
mvn clean compile exec:java
```

By default, the application will be attached to port 4567.

### Tests

Tests are written using cucumber-java and work as integration tests (no unit provided at the moment). As they are integration
 tests, they are bound to *verify* phase in maven. So, the following command should be executed for running them: 
 
```
mvn clean verify
```

## Improvements

- Only USD currency is supported at the moment. In order to support other currencies, it should invoke a currency 
conversion service.
- Include unit tests (only integration tests are provided)
- Currently there is only a implementation that uses an in memory database.



