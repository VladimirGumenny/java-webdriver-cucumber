package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.List;

import static support.TestContext.getWait;

public class NewCandidatePage extends Header {

    public NewCandidatePage() {
        setHeaderText("Apply");
    }

    @FindBy(xpath = "//label[@for='candidateFirstName']/../input")
    private WebElement candidateFirstName;

    @FindBy(xpath = "//label[@for='candidateMiddleName']/../input")
    private WebElement candidateMiddleName;

    @FindBy(xpath = "//label[@for='candidateLastName']/../input")
    private WebElement candidateLastName;

    @FindBy(xpath = "//label[@for='candidateEmail']/../input")
    private WebElement candidateEmail;

    @FindBy(xpath = "//label[@for='candidatePassword']/../input")
    private WebElement candidatePassword;

    @FindBy(xpath = "//label[@for='candidateConfirmPassword']/../input")
    private WebElement candidateConfirmPassword;

    @FindBy(xpath = "//label[@for='candidateSummary']/../textarea")
    private WebElement candidateSummary;

    @FindBy(xpath = "//label[@for='candidateAddress']/../input")
    private WebElement candidateAddress;

    @FindBy(xpath = "//label[@for='candidateCity']/../input")
    private WebElement candidateCity;

    @FindBy(xpath = "//label[@for='candidateZip']/../input")
    private WebElement candidateZip;

    @FindBy(xpath = "//label[@for='candidateState']/..//select")
    private WebElement candidateState;

    @FindBy(xpath = "//button[@id='candidateSubmit']")
    private WebElement candidateSubmitButton;

    @FindBy(xpath = "//li[@class='list-item card list li-selected']")
    private List<WebElement> myJobs;

    public void fillCandidateDetails(HashMap<String, String> candidate) {
        sendKeys(candidateFirstName, candidate.get("firstName"));
        String middleName = "";
        middleName = candidate.get("middleName");
        if (middleName != null) {
            sendKeys(candidateMiddleName, middleName);
        }
        sendKeys(candidateLastName, candidate.get("lastName"));
        sendKeys(candidateEmail, candidate.get("email"));
        sendKeys(candidatePassword, candidate.get("password"));
        sendKeys(candidateConfirmPassword, candidate.get("password"));
        sendKeys(candidateSummary, candidate.get("summary"));
        sendKeys(candidateAddress, candidate.get("address"));
        sendKeys(candidateCity, candidate.get("city"));
        sendKeys(candidateZip, candidate.get("zip"));
        new Select(candidateState).selectByValue(candidate.get("state"));
    }

    public void clickCandidateSubmitButton() {
        click(candidateSubmitButton);
        getWait().until(ExpectedConditions.visibilityOfAllElements(myJobs));
    }
}
