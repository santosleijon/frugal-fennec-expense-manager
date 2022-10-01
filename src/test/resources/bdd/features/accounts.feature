Feature: Account management

  Scenario: Accounts can be viewed
    Given a user with email "test@example.com" has logged in
    And an account with the name "Account 1"
    And an account with the name "Account 2"
    When the user opens the accounts page
    Then the account "Account 1" is displayed in the accounts list
    And the account "Account 2" is displayed in the accounts list

  Scenario: Accounts can only be retrieved by a user with a valid user session cookie
    When accounts are retrieved without a valid user session cookie
    Then an InvalidSessionToken error is returned

  #TODO: Scenario: Only accounts belonging to the logged-in users can be viewed

  Scenario: An account can be created
    Given a user with email "test@example.com" has logged in
    When the user opens the accounts page
    And the user enters account name "Account 1"
    And the user clicks on "Add"
    Then the account "Account 1" is displayed in the accounts list

  Scenario: An account can only be created by a user with a valid user session cookie
    When an account is created without a valid user session cookie
    Then an InvalidSessionToken error is returned

  Scenario: An account can be renamed
    Given a user with email "test@example.com" has logged in
    And an account with the name "Old account"
    When the user opens the accounts page
    And the user enters new account name "New account" into the account name cell for "Old account"
    Then the account "New account" is displayed in the accounts list

  Scenario: An account can only be renamed by a user with a valid user session cookie
    When an account is renamed without a valid user session cookie
    Then an InvalidSessionToken error is returned

  #TODO: Scenario: Only an account belonging to the logged-in user can be renamed

  Scenario: An account can be deleted
    Given a user with email "test@example.com" has logged in
    And an account with the name "Account 1"
    When the user opens the accounts page
    And the user selects "Account 1" in the accounts list
    And the user clicks on "Delete accounts"
    Then the account "Account 1" is not displayed in the accounts list

  Scenario: An account can be only be deleted by a user with a valid user session cookie
    When an account is deleted without a valid user session cookie
    Then an InvalidSessionToken error is returned

  #TODO: Scenario: Only an account belonging to the logged-in user can be deleted

  Scenario: Multiple accounts can be deleted at he same time
    Given a user with email "test@example.com" has logged in
    And an account with the name "Account 1"
    And an account with the name "Account 2"
    When the user opens the accounts page
    And the user selects "Account 1" in the accounts list
    And the user selects "Account 2" in the accounts list
    And the user clicks on "Delete accounts"
    Then the account "Account 1" is not displayed in the accounts list
    And the account "Account 1" is not displayed in the accounts list
