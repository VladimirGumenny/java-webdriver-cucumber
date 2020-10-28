package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.HashMap;

public class ProfilePage extends Header {

    public ProfilePage() {
        setHeaderText("Profile");
    }

    @FindBy(xpath = "//label[@for='candidateFirstName']/../span")
    private WebElement candidateFirstName;

    @FindBy(xpath = "//label[@for='candidateMiddleName']/../span")
    private WebElement candidateMiddleName;

    @FindBy(xpath = "//label[@for='candidateLastName']/../span")
    private WebElement candidateLastName;

    @FindBy(xpath = "//label[@for='candidateEmail']/../span")
    private WebElement candidateEmail;

    @FindBy(xpath = "//label[@for='candidatePassword']/../span")
    private WebElement candidatePassword;

    @FindBy(xpath = "//label[@for='candidateSummary']/../span")
    private WebElement candidateSummary;

    @FindBy(xpath = "//label[@for='candidateAddress']/../span")
    private WebElement candidateAddress;

    @FindBy(xpath = "//label[@for='candidateCity']/../span")
    private WebElement candidateCity;

    @FindBy(xpath = "//label[@for='candidateState']/../span")
    private WebElement candidateState;

    @FindBy(xpath = "//label[@for='candidateZip']/../span")
    private WebElement candidateZip;

    public boolean isProfileCorrect(HashMap<String, String> candidate) {
        String middleName = "";
        middleName = candidate.get("middleName");
        if (middleName == null) {
            middleName = "";
        }
        return
                candidateFirstName.getText().equals(candidate.get("firstName")) &&
                candidateMiddleName.getText().equals(middleName) &&
                candidateLastName.getText().equals(candidate.get("lastName")) &&
                candidateEmail.getText().equals(candidate.get("email")) &&
                !candidatePassword.getText().equals(candidate.get("password")) &&
                candidateSummary.getText().equals(candidate.get("summary")) &&
                candidateAddress.getText().equals(candidate.get("address")) &&
                candidateCity.getText().equals(candidate.get("city")) &&
                candidateZip.getText().equals(candidate.get("zip"));

    }
}
