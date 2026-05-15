Feature: Change password

  Scenario: Change password
    Given A user is logged in
    Then I will see a HTTP OK response
    When I change password with an incorrect original password
    Then I will see a PASSWORD_NOT_CORRECT error
    When I change the password
    Then I will see a HTTP OK response