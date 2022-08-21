Feature: Password-less user registration and login using email verification

  Scenario: A new user can be registered by logging in
    Given that the randomly generated email verification code will be "1234"
    When the user opens the login page
    And the user enters email "test@example.com"
    And the user clicks on "Start login"
    Then an email with verification code "1234" is sent to "test@example.com"
    And the user sees the complete login form
    When the user enters email verification code "1234"
    And the user clicks on "Complete login"
    Then a user is created for email "test@example.com"
    And a user session is created for user with email "test@example.com"
    And the user is redirected to the reports page

  Scenario: An existing user can log in
    Given a registered user "test@example.com"
    And that the randomly generated email verification code will be "1234"
    When the user opens the login page
    And the user enters email "test@example.com"
    And the user clicks on "Start login"
    And the user enters email verification code "1234"
    And the user clicks on "Complete login"
    Then a user session is created for user with email "test@example.com"
    And the user is redirected to the reports page

  Scenario: An error is displayed when trying to start a login with an invalid email address
    When the user opens the login page
    And the user enters email "@invalid-email-address"
    And the user clicks on "Start login"
    Then the user sees the error message "Invalid email address"

  Scenario: An error is displayed when trying to complete the login with an invalid email verification code
    Given that the randomly generated email verification code will be "1234"
    When the user opens the login page
    And the user enters email "test@example.com"
    And the user clicks on "Start login"
    And the user enters email verification code "5678"
    And the user clicks on "Complete login"
    Then the user sees the error message "Invalid verification code"

  Scenario: An error is displayed when trying to complete the login with the same email verification code twice
    Given that the randomly generated email verification code will be "1234"
    When the user opens the login page
    And the user enters email "test@example.com"
    And the user clicks on "Start login"
    And a user with email "test@example.com" completes a login with verification code "1234"
    And the user enters email verification code "5678"
    And the user clicks on "Complete login"
    Then the user sees the error message "Invalid verification code"

  Scenario: A started login can be aborted
    When the user opens the login page
    And the user enters email "test@example.com"
    And the user clicks on "Start login"
    And the user clicks on "Abort"
    Then the user is redirected back the start login form
    And no unconsumed email verification codes exist for user with email "test@example.com"

  # TODO: Logout

  # TODO: Max allowed started logins
