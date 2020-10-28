package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import support.TestContext;

import java.util.List;

import static support.TestContext.getDriver;
import static support.TestContext.getWait;

public class LandingPage extends Header {

    public LandingPage() {
        setUrl("http://skryabin-careers.herokuapp.com/");
        setHeaderText("Careers");
    }

    @FindBy(xpath = "//li")
    private List<WebElement> positionCards;

    @FindBy(xpath = "//li[not(contains(@class,'selected'))]")
    private List<WebElement> notSelectedPositionCards;

    private String getLastPositionId() {
        int lastId = Integer.parseInt(positionCards.get(0).getAttribute("id"));
        for (WebElement card : positionCards) {
            int id = Integer.parseInt(card.getAttribute("id"));
            if (lastId < id) {
                lastId = id;
            }
        }
        return String.valueOf(lastId);
    }

    private WebElement getLastPositionElement() {
        return getElementByPositionId(getLastPositionId());
    }

    private WebElement getElementByPositionId(String positionId) {
        return getDriver().findElement(By.xpath("//li[@id='" + positionId + "']"));
    }

    private WebElement buttonApplyToNewPosition() {
        return buttonApplyToPositionById(getLastPositionId());
    }

    private WebElement buttonApplyToPositionById(String positionId) {
        return getDriver().findElement(By.xpath("//li[@id='" + positionId + "']//button"));
    }

    public void clickApplyNewPosition() {
        WebElement lastPosition = getLastPositionElement();
        String lastPositionId = getLastPositionId();
        new Actions(getDriver()).moveToElement(lastPosition).perform();
        click(buttonApplyToNewPosition());
        TestContext.setTestData("appliedPositionId", lastPositionId);
    }

    public List<WebElement> getNotSelectedPositionCards() {
        return notSelectedPositionCards;
    }

    public void clickApplyToPositionById(String positionId) {
        click(buttonApplyToPositionById(positionId));
        getWait().until(ExpectedConditions.attributeContains(getElementByPositionId(positionId), "class", "selected"));
    }

    public String getIdByElement(WebElement position) {
        return position.getAttribute("id");
    }

    public boolean isPositionSelected(String id) {
        String classAttr = getElementByPositionId(id).getAttribute("class");
        return classAttr.contains("selected");
    }
}
