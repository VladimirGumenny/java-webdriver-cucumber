@careers
  Feature: Careers scenarios

    @careers1
    Scenario: Recruiter creates position
      Given I open "careers" page
      And I login as "recruiter"
      Then I verify "recruiter" login
      When I create new position
      And I verify position created

    @careers2 @create_position @delete_position
    Scenario: Careers candidate scenario
      Given I open "careers" page
      And I apply to a new position
      Then I verify profile is created
      And I see position in my jobs

    @careers3
    Scenario: Careers adds new job
      Given I open "careers" page
      And I login as "candidate"
      Then I verify "candidate" login
      When I apply for a new job
      Then I see position marked as applied
      And I see position in my jobs

    @careers4
    Scenario: REST API
      Given I login to REST as "recruiter"
      When I create a new position with POST
      Then I verify via REST new position in the list
      And I update via REST new position
      Then I verify via REST position details
      And I verify position's metadata via REST
      When I create a new candidate via REST
      Then I verify a new candidate in the list via REST
      When I update a new candidate via REST
      Then I verify candidate details via REST
      When I add resume to the candidate via REST
      Then I verify that resume is attached via REST
      When I apply a new candidate to a new position via REST
      Then I verify a new application in the list via REST
      When I update a new application via REST
      Then I verify new application details via REST
      When I delete new application via REST
      And I delete a new position with DELETE
      And I delete resume of candidate via REST
      And I delete a new candidate via REST