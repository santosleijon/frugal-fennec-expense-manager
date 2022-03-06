Feature: Expense management

  Scenario: Expenses can be viewed
    Given an account with the name "Account 1"
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
    Given an account with the name "Account 1"
    When the user opens the expenses page
    And the user enters expense date "2022-01-01" in the add expense form
    And the user selects account "Account 1" in the add expense form
    And the user enters expense description "A new expense" in the add expense form
    And the user enters amount 1.00 in the add expense form
    And the user clicks on "Add"
    Then the following expenses are displayed in the expenses list
      | date                  | description          | amount  |
      | 2022-01-01T00:00:00Z  | A new expense        | 1.00    |

  Scenario: An expense can be deleted
    Given an account with the name "Account 1"
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

  Scenario: Multiple expenses can be deleted at the same time
    Given an account with the name "Account 1"
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
