package definitions;

import com.mashape.unirest.http.exceptions.UnirestException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;
import pages.*;
import support.RestWrapper;
import support.TestContext;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static support.RestWrapper.assertObjectsEqual;
import static support.TestContext.*;

public class CareersStepDefs {
    @Given("^I open \"([^\"]*)\" page$")
    public void iOpenPage(String page) throws Exception {

        switch (page) {
            case "careers":
                new LandingPage().open();
                break;
            default:
                throw new Exception("Page " + page + " not recognized!!");
        }
    }

    @And("^I login as \"([^\"]*)\"$")
    public void iLoginAs(String role) throws Throwable {
        HashMap<String, String> user = getData(role);
        LandingPage landingPage = new LandingPage();
        landingPage.assertHeader();
        landingPage.clickLogin();
        LoginPage loginPage = new LoginPage();
        loginPage.assertHeader();
        loginPage.login(user.get("email"), user.get("password"));
    }

    @Then("^I verify \"([^\"]*)\" login$")
    public void iVerifyLogin(String role) throws Throwable {
        String username = new LoginPage().getUsername();
        HashMap<String, String> user = getData(role);
        assertThat(username.equals(user.get("username"))).isTrue();
    }

    @When("^I create new position$")
    public void iCreateNewPosition() throws Exception {
        new LandingPage().clickButtonRecriut();
        RecruiterPage recruit = new RecruiterPage();
        recruit.assertHeader();
        recruit.clickCreateNewPosition();

        NewPositionPage newPositionPage = new NewPositionPage();
        newPositionPage.assertHeader();

        HashMap<String, String> position = TestContext.getPosition();
        String title = position.get("title");
        title = TestContext.addTimeStamp(title);
        TestContext.setTestData("title", title);
        position.put("title", title);
        newPositionPage.fillPosition(position);

        newPositionPage.clickPositionSubmit();
    }

    @And("^I verify position created$")
    public void iVerifyPositionCreated() {
        RecruiterPage recruit = new RecruiterPage();
        assertThat(recruit.isPositionPresentOnOPage(TestContext.getStringTestData("title"))).isTrue();
    }

    @And("^I apply to a new position$")
    public void iApplyToANewPosition() throws Exception{
        new LandingPage().clickApplyNewPosition();
        HashMap<String, String> candidate = getCandidate();
        NewCandidatePage newCandidatePage = new NewCandidatePage();
        newCandidatePage.fillCandidateDetails(candidate);
        newCandidatePage.clickCandidateSubmitButton();
    }

    @Then("^I verify profile is created$")
    public void iVerifyProfileIsCreated() throws Exception{
        HashMap<String, String> candidate = getCandidate();
        LandingPage landingPage = new LandingPage();
        String candidateName = landingPage.getUsername();
        String middleName = candidate.get("middleName");
        if (middleName != null) {
            middleName += " ";
        } else {
            middleName = "";
        }
        String actualName = candidate.get("firstName") + " " + middleName + candidate.get("lastName");
        assertThat(candidateName.equals(actualName)).isTrue();
        landingPage.clickUserName();
        ProfilePage profile = new ProfilePage();
        profile.assertHeader();
        assertThat(profile.isProfileCorrect(candidate)).isTrue();
    }

    @And("^I see position in my jobs$")
    public void iSeePositionInMyJobs() throws Exception {
        new LandingPage().clickMyJobs();
        MyJobsPage jobs = new MyJobsPage();
        jobs.assertHeader();
        String appliedPositionId = TestContext.getStringTestData("appliedPositionId");
        assertThat(jobs.isPositionInJobs(appliedPositionId)).isTrue();
    }

    @When("^I apply for a new job$")
    public void iApplyForANewJob() {
        LandingPage jobsPage = new LandingPage();
        List<WebElement> positionsNotSelected = jobsPage.getNotSelectedPositionCards();
        int randomPosition = new Random().nextInt(positionsNotSelected.size() - 1);
        String positionId = jobsPage.getIdByElement(positionsNotSelected.get(randomPosition));
        jobsPage.clickApplyToPositionById(positionId);
        TestContext.setTestData("appliedPositionId", positionId);
    }

    @Then("^I see position marked as applied$")
    public void iSeePositionMarkedAsApplied() throws Exception {
        LandingPage jobsPage = new LandingPage();
        String appliedPositionId = TestContext.getStringTestData("appliedPositionId");
        Thread.sleep(1000);
        assertThat(jobsPage.isPositionSelected(appliedPositionId)).isTrue();
    }

    @Given("^I login to REST as \"([^\"]*)\"$")
    public void iLoginToRESTAs(String role) throws Exception {
        HashMap<String, String> user = getData(role);
        new RestWrapper().login(user);
    }

    @When("^I create a new position with POST$")
    public void iCreateANewPositionWithPOST() throws Exception{
        HashMap<String, String> position = getPositionWithTimestamp();
        new RestWrapper().createPosition(position);
    }

    @And("^I delete a new position with DELETE$")
    public void iDeleteANewPositionWithDELETE() throws Exception {
        int positionId = getJsonTestData(RestWrapper.POSITION).getInt("id");
        new RestWrapper().deletePosition(positionId);
    }

    @Then("^I verify via REST new position in the list$")
    public void iVerifyViaRESTNewPositionInTheList() throws Exception {
        JSONArray actualPositions = new RestWrapper().getPositions();
        JSONObject expectedPosition = getJsonTestData(RestWrapper.POSITION);

        boolean isFound = false;
        for (int i = 0; i < actualPositions.length(); i++) {
            JSONObject actualPosition = actualPositions.getJSONObject(i);
            if (actualPosition.getInt("id") == expectedPosition.getInt("id")) {
                isFound = true;
                break;
            }
        }
        assertThat(isFound).isTrue();
    }

    @And("^I update via REST new position$")
    public void iUpdateViaRESTNewPosition() throws Exception {
        HashMap<String, String> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("city", "Mountain View");
        int positionId = getJsonTestData(RestWrapper.POSITION).getInt("id");

        JSONObject response = new RestWrapper().updatePosition(fieldsToUpdate, positionId);
        assertThat(response.getString("city")).isEqualTo("Mountain View");

        JSONObject position = getJsonTestData(RestWrapper.POSITION);
        position.put("city", "Mountain View");
        setTestData(RestWrapper.POSITION, position);
    }

    @Then("^I verify via REST position details$")
    public void iVerifyViaRESTPositionDetails() throws Exception {
        JSONObject expectedPosition = getJsonTestData(RestWrapper.POSITION);
        int positionId = expectedPosition.getInt("id");
        JSONObject actualPosition = new RestWrapper().getPositionById(positionId);

        assertObjectsEqual(actualPosition, expectedPosition);
    }

    @When("^I create a new candidate via REST$")
    public void iCreateANewCandidateViaREST() throws Exception {
        HashMap<String, String> candidate = getCandidateWithTimestamp();
        new RestWrapper().createCandidate(candidate);
    }

    @And("^I delete a new candidate via REST$")
    public void iDeleteANewCandidateViaREST() throws UnirestException {
        JSONObject candidateJson = getJsonTestData(RestWrapper.CANDIDATES);
        int candidateId = candidateJson.getInt("id");
        new RestWrapper().deleteCandidate(candidateId);
    }

    @Then("^I verify a new candidate in the list via REST$")
    public void iVerifyANewCandidateInTheListViaREST() throws UnirestException {
        JSONArray actualCandidates = new RestWrapper().getCandidates();
        JSONObject expectedCandidate = getJsonTestData(RestWrapper.CANDIDATES);

        boolean isFound = false;
        for(int i = 0; i < actualCandidates.length(); i++) {
            if(actualCandidates.getJSONObject(i).getInt("id") == expectedCandidate.getInt("id")) {
                isFound = true;
                break;
            }
        }
        assertThat(isFound).isTrue();
    }

    @When("^I update a new candidate via REST$")
    public void iUpdateANewCandidateViaREST() throws UnirestException {
        HashMap<String, String> fieldsToUpdate = new HashMap<>();
        String keyToUpdate = "summary";
        String valueToUpdate = "Very good candidate";
        fieldsToUpdate.put(keyToUpdate, valueToUpdate);
        int candidateId = getJsonTestData(RestWrapper.CANDIDATES).getInt("id");

        JSONObject response = new RestWrapper().updateCandidate(fieldsToUpdate, candidateId);

        assertThat(response.getString(keyToUpdate)).isEqualTo(valueToUpdate);

        JSONObject candidate = getJsonTestData(RestWrapper.CANDIDATES);
        candidate.put(keyToUpdate, valueToUpdate);
        setTestData(RestWrapper.CANDIDATES, candidate);
    }

    @Then("^I verify candidate details via REST$")
    public void iVerifyCandidateDetailsViaREST() throws UnirestException {
        JSONObject expectedCandidate = getJsonTestData(RestWrapper.CANDIDATES);
        int candidateId = expectedCandidate.getInt("id");
        JSONObject actualCandidate = new RestWrapper().getCandidateById(candidateId);

        Set<String> expectedKeys = expectedCandidate.keySet();
        for (String key : expectedKeys) {
            if (!key.equals("password") && !key.equals("id")) {
                String expectedValue = expectedCandidate.getString(key);
                String actualValue = actualCandidate.getString(key);
                assertThat(actualValue.equals(expectedValue))
                        .isTrue()
                        .overridingErrorMessage("\n\n*** Key: " + key + ", expected value: " + expectedValue + ", actual value: " + actualValue);
                assertThat(actualCandidate.get(key).getClass())
                        .isEqualTo(expectedCandidate.get(key).getClass());
            }
        }
    }

    @When("^I apply a new candidate to a new position via REST$")
    public void iApplyANewCandidateToANewPositionViaREST() throws Exception {
        JSONObject position = getJsonTestData(RestWrapper.POSITION);
        JSONObject candidate = getJsonTestData(RestWrapper.CANDIDATES);
        new RestWrapper().applyCandidateToPosition(position, candidate);
    }

    @When("^I delete new application via REST$")
    public void iDeleteNewApplicationViaREST() throws UnirestException {
        JSONObject application = getJsonTestData(RestWrapper.APPLICATIONS);
        new RestWrapper().deleteApplication(application.getInt("id"));
    }

    @Then("^I verify a new application in the list via REST$")
    public void iVerifyANewApplicationInTheListViaREST() throws UnirestException {
        JSONArray actualApplications = new RestWrapper().getApplications();
        int expectedApplicationId = TestContext.getJsonTestData(RestWrapper.APPLICATIONS).getInt("id");

        boolean isFound = false;
        for (int i = 0; i < actualApplications.length(); i++) {
            int actualApplicationId = actualApplications.getJSONObject(i).getInt("id");
            if (actualApplicationId == expectedApplicationId) {
                isFound = true;
                break;
            }
        }
        assertThat(isFound).isTrue();
    }

    @When("^I update a new application via REST$")
    public void iUpdateANewApplicationViaREST() throws UnirestException {
        JSONObject applicationJson = getJsonTestData(RestWrapper.APPLICATIONS);
        int applicationId = applicationJson.getInt("id");
        String keyToUpdate = "positionID";
        String positionId = String.valueOf(applicationJson.get(keyToUpdate));
        String valueToUpdate = "1";
        HashMap<String, String> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put(keyToUpdate, valueToUpdate);

        RestWrapper rest = new RestWrapper();
        JSONObject response = rest.updateApplication(fieldsToUpdate, applicationId);
        assertThat(response.getString(keyToUpdate)).isEqualTo(valueToUpdate);

        System.out.println("\n\nApplication " + applicationId + " was updated.");

        fieldsToUpdate.put(keyToUpdate, positionId);

        response = rest.updateApplication(fieldsToUpdate, applicationId);
        assertThat(response.getString(keyToUpdate)).isEqualTo(positionId);
    }

    @Then("^I verify new application details via REST$")
    public void iVerifyNewApplicationDetailsViaREST() throws UnirestException {
        JSONObject expectedApplication = getJsonTestData(RestWrapper.APPLICATIONS);
        int applicationId = expectedApplication.getInt("id");
        int positionId = expectedApplication.getInt("positionID");
        int candidateId = expectedApplication.getInt("candidateID");
        RestWrapper rest = new RestWrapper();
        JSONObject actualApplication = rest.getApplicationById(applicationId);
        JSONObject actualPosition = rest.getPositionById(positionId);
        JSONObject expectedPosition = getJsonTestData(RestWrapper.POSITION);
        assertThat(expectedPosition.getInt("id")).isEqualTo(actualPosition.getInt("id"));
        JSONObject actualCandidate = rest.getCandidateById(candidateId);
        JSONObject expectedCandidate = getJsonTestData(RestWrapper.CANDIDATES);
        assertThat(expectedCandidate.getInt("id")).isEqualTo(actualCandidate.getInt("id"));

        Set<String> keys = actualApplication.keySet();
        for (String key : keys) {
            String keyToVerify;
            switch (key) {
                case "middleName":
                    if (expectedCandidate.isNull(key)) {
                        assertThat(actualApplication.isNull(key));
                        break;
                    }
                case "firstName":
                case "lastName":
                case "email":
                case "summary":
                case "candidate_address":
                case "candidate_city":
                case "candidate_state":
                case "candidate_zip":
                    keyToVerify = key.replace("candidate_", "");
                    assertThat(actualApplication.get(key))
                            .isEqualTo(expectedCandidate.get(keyToVerify))
                            .overridingErrorMessage("\n\n*** keyApp=" + key + ", key candidate=" + keyToVerify);
                    assertThat(actualApplication.get(key).getClass())
                            .isEqualTo(expectedCandidate.get(keyToVerify).getClass());
                    break;
                case "title":
                case "description":
                case "dateOpen":
                case "position_address":
                case "position_city":
                case "position_state":
                case "position_zip":
                    keyToVerify = key.replace("position_", "");
                    assertThat(actualApplication.get(key))
                            .isEqualTo(expectedPosition.get(keyToVerify))
                            .overridingErrorMessage("\n\n*** keyApp=" + key + ", key position=" + keyToVerify);
                    assertThat(actualApplication.get(key).getClass())
                            .isEqualTo(expectedPosition.get(keyToVerify).getClass());
                    break;
            }
        }
    }

    @When("^I add resume to the candidate via REST$")
    public void iAddResumeToTheCandidateViaREST() throws Exception {
        int candidateId = getJsonTestData(RestWrapper.CANDIDATES).getInt("id");
        new RestWrapper().addResumeToCandidate(candidateId);
    }

    @Then("^I verify that resume is attached via REST$")
    public void iVerifyThatResumeIsAttachedViaREST() throws UnirestException {
        int candidateId = getJsonTestData(RestWrapper.CANDIDATES).getInt("id");
        new RestWrapper().isResumeAttached(candidateId);
    }

    @And("^I delete resume of candidate via REST$")
    public void iDeleteResumeOfCandidateViaREST() throws UnirestException {
        int candidateId = getJsonTestData(RestWrapper.CANDIDATES).getInt("id");
        new RestWrapper().deleteResume(candidateId);
    }

    @And("^I verify position's metadata via REST$")
    public void iVerifyPositionSMetadataViaREST() throws Exception {
        RestWrapper rest = new RestWrapper();

        JSONObject expectedPosition = getJsonTestData(RestWrapper.POSITION);
        int positionId = expectedPosition.getInt("id");
        JSONObject actualPosition = new RestWrapper().getPositionById(positionId);

        Set<String> keys = expectedPosition.keySet();
        for (String key : keys) {
             String expectedType = getSwaggerType(RestWrapper.POSITIONS, key);
             String actualType = actualPosition.get(key).getClass().getTypeName();
             actualType = actualType.toLowerCase().replace("java.lang.","");
             assertThat(expectedType)
                     .isEqualTo(actualType)
                     .overridingErrorMessage("Key: " + key + ". \tExpected type: " + expectedType + " , actual type: " + actualType);
        }
    }
}
