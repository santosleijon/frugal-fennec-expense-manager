Feature: Expense report creation

  Scenario: Expense reports can be created and viewed
    Given an account with the name "Account 1"
    And the account with the name "Account 1" has the following expenses
      | date                  | description | amount  |
      | 2022-01-01T00:00:00Z  | Expense 1   | 1.00    |
      | 2022-01-02T00:00:00Z  | Expense 2   | 1.00    |
      | 2022-01-02T00:00:00Z  | Expense 3   | 1.00    |
      | 2022-01-03T00:00:00Z  | Expense 4   | 1.00    |
      | 2022-01-03T00:00:00Z  | Expense 5   | 1.00    |
      | 2022-01-03T00:00:00Z  | Expense 6   | 1.00    |
    When the user opens the reports page
    Then the expense report shown contains the following values
      | date        | amount  |
      | 2022-01-01  | 1.0     |
      | 2022-01-02  | 2.0     |
      | 2022-01-03  | 3.0     |
