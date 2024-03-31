Feature: Reserve a Activity

  A Customer can reserve a Activity by providing his credit card details

  Scenario: Reserve a Activity
    Given a Customer named "John" with the credit card "1230896983"
    And a Partner named "Tom"
    And a Activity named "Trekking", "Trekking in the montains" with a price of 100
    When he adds this activity to his cart for the date "07-11 21:30"
    And he proceeds to checkout
    Then a transaction of 100 should appear on the Transaction History of "John"
    And the Activity "Trekking" should be reserved

