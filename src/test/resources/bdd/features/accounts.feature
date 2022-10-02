Feature: Account management

  Scenario: Accounts can be viewed
    Given the user with email "test@example.com" has logged in
    And user "test@example.com" has an account with the name "Account 1"
    And user "test@example.com" has an account with the name "Account 2"
    When the user opens the accounts page
    Then the account "Account 1" is displayed in the accounts list
    And the account "Account 2" is displayed in the accounts list

  Scenario: Accounts can only be retrieved by a user with a valid user session cookie
    When accounts are retrieved without a valid user session cookie
    Then an InvalidSessionToken error is returned

  Scenario: Only accounts belonging to the logged-in users can be viewed
    Given a registered user "user-1@example.com"
    And a registered user "user-2@example.com"
    And user "user-1@example.com" has an account with the name "User 1s account"
    And user "user-2@example.com" has an account with the name "User 2s account"
    And the user with email "user-1@example.com" has logged in
    When the user opens the accounts page
    Then the account "User 2s account" is not displayed in the accounts list

  Scenario: An account can only be retrieved by the user who it belongs to
    Given a registered user "user-1@example.com"
    And a registered user "user-2@example.com"
    And user "user-1@example.com" has an account with the name "User 1s account"
    When user "user-2@example.com" tries to retrieve the account "User 1s account"
    Then an AccountNotFound error is returned

  Scenario: An account can be created
    Given the user with email "test@example.com" has logged in
    When the user opens the accounts page
    And the user enters account name "Account 1"
    And the user clicks on "Add"
    Then the account "Account 1" is displayed in the accounts list

  Scenario: An account can only be created by a user with a valid user session cookie
    When an account is created without a valid user session cookie
    Then an InvalidSessionToken error is returned

  Scenario: An account can be renamed
    Given the user with email "test@example.com" has logged in
    And user "test@example.com" has an account with the name "Old account"
    When the user opens the accounts page
    And the user enters new account name "New account" into the account name cell for "Old account"
    Then the account "New account" is displayed in the accounts list

  Scenario: An account can only be renamed by a user with a valid user session cookie
    When an account is renamed without a valid user session cookie
    Then an InvalidSessionToken error is returned

  Scenario: An account can only be renamed by the user who it belongs to
    Given a registered user "user-1@example.com"
    And a registered user "user-2@example.com"
    And user "user-1@example.com" has an account with the name "User 1s account"
    When user "user-2@example.com" tries to rename the account "User 1s account" to "User 1s account renamed"
    Then an UnauthorizedOperation error is returned

  Scenario: An account can be deleted
    Given the user with email "test@example.com" has logged in
    And user "test@example.com" has an account with the name "Account 1"
    When the user opens the accounts page
    And the user selects "Account 1" in the accounts list
    And the user clicks on "Delete accounts"
    Then the account "Account 1" is not displayed in the accounts list

  Scenario: An account can be only be deleted by a user with a valid user session cookie
    When an account is deleted without a valid user session cookie
    Then an InvalidSessionToken error is returned

  #TODO: Scenario: Only an account belonging to the logged-in user can be deleted

  Scenario: Multiple accounts can be deleted at he same time
    Given the user with email "test@example.com" has logged in
    And user "test@example.com" has an account with the name "Account 1"
    And user "test@example.com" has an account with the name "Account 2"
    When the user opens the accounts page
    And the user selects "Account 1" in the accounts list
    And the user selects "Account 2" in the accounts list
    And the user clicks on "Delete accounts"
    Then the account "Account 1" is not displayed in the accounts list
    And the account "Account 1" is not displayed in the accounts list
