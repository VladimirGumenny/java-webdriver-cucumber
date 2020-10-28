package pages;

import org.openqa.selenium.By;

import static support.TestContext.getDriver;

public class MyJobsPage extends Header {

    public MyJobsPage() {
        setHeaderText("My Jobs");
    }

    public boolean isPositionInJobs(String id) {
        return !getDriver().findElements(By.xpath("//li[@id='" + id + "']")).isEmpty();
    }
}
