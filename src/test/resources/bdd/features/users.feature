Feature: User registration and login

  Scenario: User receives a verification code by email when logging in
    When a user with email "test@example.com" starts logging in
    Then an email with a verification code is sent to "test@example.com"