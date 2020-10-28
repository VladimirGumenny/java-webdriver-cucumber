package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

import static support.TestContext.getDriver;

public class Dokkio {

    private String url;

    public Dokkio() {
        PageFactory.initElements(getDriver(), this);
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void open() {
        getDriver().get(url);
    }

    public boolean isJobPresent(String jobToVerify) {
        return !getDriver().findElements(By.xpath("//div[@id='explainer']//h3[contains(text(),'" + jobToVerify + "')]")).isEmpty();
    }

    public void clickButtonHeader(String buttonName) {
        getDriver().findElement(By.xpath("//header[@id='top']//a[contains(@href,'" + buttonName.toLowerCase() + "')]")).click();
    }

}
