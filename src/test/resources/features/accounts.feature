Feature: Accounts functionality

  Scenario: An account can be created
    When an account is created with the name "Account 1"
    Then an account with the name "Account 1" exists

  Scenario: An account can be created
    Given an account with the name "Old account"
    When account "Old account" is renamed to "New account"
    Then an account with the name "New account" exists
    And an account with the name "Old account" does not exist

  Scenario: An account can be deleted
    Given an account with the name "Account 1"
    When the account with the name "Account 1" is deleted
    Then an account with the name "Account 1" does not exist

  Scenario: All accounts can be retrieved
    Given an account with the name "Account 1"
    * an account with the name "Account 2"
    * an account with the name "Account 3"
    When all accounts are retrieved
    Then the retrieved list of accounts contains the following account names:
      | Account 1 |
      | Account 2 |
      | Account 3 |

  Scenario: Expenses can be added to an account
    Given an account with the name "Account 1"
    When the following expenses are added to the account with the name "Account 1":
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
      | 2022-01-02T00:00:00Z  | Another expense      | 2.00    |
      | 2022-01-03T00:00:00Z  | Yet another expense  | 3.00    |
    Then the account with the name "Account 1" has the following expenses:
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
      | 2022-01-02T00:00:00Z  | Another expense      | 2.00    |
      | 2022-01-03T00:00:00Z  | Yet another expense  | 3.00    |

  Scenario: Expenses can be deleted from an account
    Given an account with the name "Account 1" and the following expenses:
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
      | 2022-01-02T00:00:00Z  | Another expense      | 2.00    |
    When the following expenses are deleted from the account with the name "Account 1":
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
    Then the account with the name "Account 1" does not have the following expenses:
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | An expense           | 1.00    |
    But the account with the name "Account 1" has the following expenses:
      | date                  | description          | amount  |
      | 2022-01-02T00:00:00Z  | Another expense      | 2.00    |
