package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.time.Year;
import java.util.HashMap;
import java.util.List;

import static support.TestContext.getDriver;
import static support.TestContext.getWait;

public class NewPositionPage extends Header {

    public NewPositionPage() {
        setHeaderText("Open Position");
    }

    @FindBy(xpath = "//label[@for='positionTitle']/../input")
    private WebElement inputPositionTitle;

    @FindBy(xpath = "//label[@for='positionDescription']/../textarea")
    private WebElement inputPositionDescription;

    @FindBy(xpath = "//label[@for='positionAddress']/../input")
    private WebElement inputPositionAddress;

    @FindBy(xpath = "//label[@for='positionCity']/../input")
    private WebElement inputPositionCity;

    @FindBy(xpath = "//label[@for='positionZip']/../input")
    private WebElement inputPositionZip;

    @FindBy(xpath = "//label[@for='positionState']/../select")
    private WebElement inputPositionState;

    @FindBy(xpath = "//button[@id='positionSubmit']")
    private WebElement positionSubmitButton;

    public void fillPosition(HashMap<String, String> position) {
        sendKeys(inputPositionTitle, position.get("title"));
        sendKeys(inputPositionDescription, position.get("description"));
        sendKeys(inputPositionAddress, position.get("address"));
        sendKeys(inputPositionCity, position.get("city"));
        sendKeys(inputPositionZip, position.get("zip"));
        new Select(inputPositionState).selectByValue(position.get("state"));
        new Calendar(position.get("dateOpen")).pickDate();
    }

    public void clickPositionSubmit() {
        click(positionSubmitButton);
    }

}
