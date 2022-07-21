Feature: User registration and login

  Scenario: User receives a verification code by email when logging in
    Given that the randomly generated email verification code will be "1234"
    When a user with email "test@example.com" starts logging in
    Then an email with verification code "1234" is sent to "test@example.com"
