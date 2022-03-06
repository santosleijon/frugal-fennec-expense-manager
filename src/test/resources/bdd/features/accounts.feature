Feature: Account management

  Scenario: Accounts can be viewed
    Given an account with the name "Account 1"
    And an account with the name "Account 2"
    When the user opens the accounts page
    Then the account "Account 1" is displayed in the accounts list
    And the account "Account 2" is displayed in the accounts list

  Scenario: An account can be created
    When the user opens the accounts page
    And the user enters account name "Account 1"
    And the user clicks on "Add"
    Then the account "Account 1" is displayed in the accounts list

  Scenario: An account can be renamed
    Given an account with the name "Old account"
    When the user opens the accounts page
    And the user enters new account name "New account" into the account name cell for "Old account"
    Then the account "New account" is displayed in the accounts list

  Scenario: An account can be deleted
    Given an account with the name "Account 1"
    When the user opens the accounts page
    And the user selects "Account 1" in the accounts list
    And the user clicks on "Delete accounts"
    Then the account "Account 1" is not displayed in the accounts list

  Scenario: Multiple accounts can be deleted at he same time
    Given an account with the name "Account 1"
    And an account with the name "Account 2"
    When the user opens the accounts page
    And the user selects "Account 1" in the accounts list
    And the user selects "Account 2" in the accounts list
    And the user clicks on "Delete accounts"
    Then the account "Account 1" is not displayed in the accounts list
    And the account "Account 1" is not displayed in the accounts list
