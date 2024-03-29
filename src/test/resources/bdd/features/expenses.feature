Feature: Expense management

  Scenario: Expenses can be viewed
    Given the user with email "test@example.com" has logged in
    And user "test@example.com" has an account with the name "Account 1"
    And the account with the name "Account 1" has the following expenses
     | date                  | description          | amount  |
     | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
     | 2022-01-02T00:00:00Z  | Another expense      | 2.00    |
     | 2022-01-03T00:00:00Z  | Yet another expense  | 3.00    |
    When the user opens the expenses page
    Then the following expenses are displayed in the expenses list
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
      | 2022-01-02T00:00:00Z  | Another expense      | 2.00    |
      | 2022-01-03T00:00:00Z  | Yet another expense  | 3.00    |

  Scenario: An expense can be added to an account
    Given the user with email "test@example.com" has logged in
    And user "test@example.com" has an account with the name "Account 1"
    When the user opens the expenses page
    And the user enters expense date "2022-01-01" in the add expense form
    And the user selects account "Account 1" in the add expense form
    And the user enters expense description "A new expense" in the add expense form
    And the user enters amount 1.00 in the add expense form
    And the user clicks on "Add"
    Then the following expenses are displayed in the expenses list
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | A new expense        | 1.00    |

  Scenario: An expense can only be added by a user with a valid user session cookie
    Given a registered user "test@example.com"
    And user "test@example.com" has an account with the name "Account 1"
    When an expense is added to account "Account 1" by a user without a valid user session cookie
    Then an InvalidSessionId error is returned

  Scenario: An expense can only be added to an account belonging to the logged-in user
    Given a registered user "user-1@example.com"
    And a registered user "user-2@example.com"
    And user "user-1@example.com" has an account with the name "User 1s account"
    When user "user-2@example.com" tries to add an expense to the account "User 1s account"
    Then an UnauthorizedOperation error is returned

  Scenario: An expense can be deleted
    Given the user with email "test@example.com" has logged in
    And user "test@example.com" has an account with the name "Account 1"
    And the account with the name "Account 1" has the following expenses
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
      | 2022-01-02T00:00:00Z  | Another expense      | 2.00    |
      | 2022-01-03T00:00:00Z  | Yet another expense  | 3.00    |
    When the user opens the expenses page
    And the user selects the following expenses from the expenses list
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
    And the user clicks on "Delete expenses"
    Then the following expenses are not displayed in the expenses list
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |

  Scenario: An expense can only be deleted by a user with a valid user session cookie
    Given a registered user "test@example.com"
    And user "test@example.com" has an account with the name "Account 1"
    And the account with the name "Account 1" has the following expenses
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
    When the expense on account "Account 1" is deleted by a user without a valid user session cookie
    Then an InvalidSessionId error is returned

  Scenario: An expense can only be deleted if it belongs to an account belonging to the logged-in user
    Given a registered user "user-1@example.com"
    And a registered user "user-2@example.com"
    And user "user-1@example.com" has an account with the name "User 1s account"
    And the account with the name "User 1s account" has the following expenses
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
    When user "user-2@example.com" tries to delete the expense on account "User 1s account"
    Then an UnauthorizedOperation error is returned

  Scenario: Multiple expenses can be deleted at the same time
    Given the user with email "test@example.com" has logged in
    And user "test@example.com" has an account with the name "Account 1"
    And the account with the name "Account 1" has the following expenses
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
      | 2022-01-02T00:00:00Z  | Another expense      | 2.00    |
      | 2022-01-03T00:00:00Z  | Yet another expense  | 3.00    |
    When the user opens the expenses page
    And the user selects the following expenses from the expenses list
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
      | 2022-01-02T00:00:00Z  | Another expense      | 2.00    |
    And the user clicks on "Delete expenses"
    Then the following expenses are not displayed in the expenses list
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
      | 2022-01-02T00:00:00Z  | Another expense      | 2.00    |
