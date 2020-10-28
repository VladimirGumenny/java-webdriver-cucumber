package definitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.Dokkio;

import static org.assertj.core.api.Assertions.assertThat;

public class DokkioStepDefs {
    @Given("^I open dokkio site$")
    public void iOpenDokkioSite() {
        Dokkio dokkio = new Dokkio();
        dokkio.setUrl("http://dokkio.com/");
        dokkio.open();
    }

    @Then("^I verify \"([^\"]*)\" job is not present on the page$")
    public void iVerifyJobIsNotPresentOnThePage(String job) {
        Dokkio dokkio = new Dokkio();
        boolean isFound = dokkio.isJobPresent(job);
        assertThat(isFound).isFalse();
    }

    @When("^I click on \"([^\"]*)\" button$")
    public void iClickOnButton(String buttonName) {
        Dokkio dokkio = new Dokkio();
        dokkio.clickButtonHeader(buttonName);
    }

    @Then("^I verify \"([^\"]*)\" job is present on the page$")
    public void iVerifyJobIsPresentOnThePage(String job) {
        Dokkio dokkio = new Dokkio();
        boolean isFound = dokkio.isJobPresent(job);
        assertThat(isFound).isTrue();
    }
}
