Feature: Take a journey

  Scenario: Store journey
    Given A journey has been completed
    When I save the journey
    Then I will see a HTTP No Content response