@dokkio
  Feature: dokkio.com testing

  @dokkio1
  Scenario: Verify that QA Engineer job listing is not displayed until you click the “Jobs” button
    Given I open dokkio site
    Then I verify "QA Engineer" job is not present on the page
    When I click on "Jobs" button
    Then I verify "QA Engineer" job is present on the page