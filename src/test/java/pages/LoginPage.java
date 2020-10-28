package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends Header {

    public LoginPage() {
        setHeaderText("Login");
    }

    @FindBy(xpath = "//label[@for='loginUsername']//../input")
    private WebElement inputUsername;

    @FindBy(xpath = "//label[@for='loginPassword']//../input")
    private WebElement inputPassword;

    @FindBy(xpath = "//button[@id='loginButton']")
    private WebElement buttonSubmit;

    public void login(String userName, String password) {
        sendKeys(inputUsername, userName);
        sendKeys(inputPassword, password);
        click(buttonSubmit);
    }

}
