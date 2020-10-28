package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

import static support.TestContext.getDriver;

public class RecruiterPage extends Header {

    public RecruiterPage() {
        setHeaderText("Recruit");
    }

    @FindBy(xpath = "//a[@href='/new_position']/h4")
    private WebElement buttonCreateNewPosition;

    public void clickCreateNewPosition() {
        click(buttonCreateNewPosition);
    }

    private List<WebElement> getPositionByTitle(String title) {
        return getDriver().findElements(By.xpath("//h4[text()='" + title + "']"));
    }

    public boolean isPositionPresentOnOPage(String title) {
        return !getPositionByTitle(title).isEmpty();
    }

}
