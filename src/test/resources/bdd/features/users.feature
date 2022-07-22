Feature: User registration and login

  Scenario: A new user is registered by completing the login using an email verification code
    Given that the randomly generated email verification code will be "1234"
    When a user with email "test@example.com" starts logging in
    Then an email with verification code "1234" is sent to "test@example.com"
    When a user with email "test@example.com" completes a login with verification code "1234"
    Then a user is created for email "test@example.com"
    And the user receives a valid session token

  # Scenario: An existing user can complete the login using an email verification code

  Scenario: An error is displayed when trying to start a login with an invalid email address
    When a user with email "@invalid-email-address" starts logging in
    Then an "InvalidEmailAddressError" error is returned

  Scenario: An error is displayed when trying to complete the login with an invalid email verification code
    Given that the randomly generated email verification code will be "1234"
    When a user with email "test@example.com" starts logging in
    And a user with email "test@example.com" completes a login with verification code "invalid-code"
    Then an "InvalidEmailVerificationCodeError" error is returned

  Scenario: An error is displayed when trying to complete a login with an invalid email address
    And a user with email "@invalid-email-address" completes a login with verification code "1234"
    Then an "InvalidEmailAddressError" error is returned
