@demo
Feature: Demonstrate the use of various browsers in a Selenium Grid setup

  @fast @chrome
  Scenario: Visit useragentstring.com using Chrome browser
    Given I use Chrome browser
    When I visit "http://www.useragentstring.com"
    Then I should see "User Agent String explained"
