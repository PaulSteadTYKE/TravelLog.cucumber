Feature: Change password

  Scenario: Change password
    Given A user is logged in
    When I change the password
    Then I will see a HTTP OK response