package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

public class Header extends Page {

    private String headerText;

    @FindBy(xpath = "//a[@href='/login']")
    private WebElement loginButton;

    @FindBy(xpath = "//span[contains(@class,'position-center')]")
    private WebElement headerTitle;

    @FindBy(xpath = "//span[@class='logout-box']/a")
    private WebElement username;

    @FindBy(xpath = "//a[@href='/recruit']/button")
    private WebElement buttonRecruit;

    @FindBy(xpath = "//a[@href='/my_jobs']")
    private WebElement myJobsButton;

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public void clickLogin() {
        click(loginButton);
    }

    public void assertHeader() {
        assertThat(headerTitle.getText().equals(headerText)).isTrue();
    }

    public void clickButtonRecriut() {
        click(buttonRecruit);
    }

    public String getUsername() {
        return username.getText();
    }

    public void clickUserName() throws InterruptedException {
        click(username);
        System.out.println("\n\n*** header title after click text: " + headerTitle.getText());
    }

    public void clickMyJobs() {
        click(myJobsButton);
    }

}
