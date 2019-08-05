Feature: Suite for checking account features

  Scenario: Creating a new money account
    Given I have a name "Jon", last name "Snow" and email "jon.snow@nightswatch.com"
    Given I have 10 dollars in my pocket
    When I ask for a new account to be created
    Then there is a new account created