package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Year;
import java.util.List;

import static support.TestContext.getDriver;

public class Calendar extends Page {

    private int year;
    private int month;
    private int day;

    public Calendar(String date) {
        clickOpenDate();
        String[] dateArr = date.split("/");
        this.year = Integer.parseInt(dateArr[2]);
        this.month = Integer.parseInt(dateArr[0]);
        this.day = Integer.parseInt(dateArr[1]);
    }

    @FindBy(xpath = "//input[@id='positionDateOpen']")
    private WebElement inputDateOpen;

    @FindBy(xpath = "//div[contains(@class,'react-datepicker__month-dropdown')]")
    private WebElement inputDateOpenMonthDropdown;

    @FindBy(xpath = "//div[@class='react-datepicker__month-option']")
    private List<WebElement> inputDateOpenMonthOptions;

    @FindBy(xpath = "//div[contains(@class,'react-datepicker__year-dropdown')]")
    private WebElement inputDateOpenYearDropdown;

    @FindBy(xpath = "//div[@class='react-datepicker__year-option']")
    private List<WebElement> inputDateOpenYearOptions;

    public void pickDate() {
        fillPositionDateOpen(this.month, this.day, this.year);
    }

    public void clickOpenDate() {
        click(inputDateOpen);
    }

    public void clickPositionDateOpenYearDropdownArrowDown() {
        inputDateOpenYearOptions.get(inputDateOpenYearOptions.size()-1).click();
    }

    public void clickPositionDateOpenYearDropdownArrowUp() {
        inputDateOpenYearOptions.get(0).click();
    }

    public void clickPositionDateOpenYearDropdownBottomYear() {
        inputDateOpenYearOptions.get(inputDateOpenYearOptions.size()-2).click();
    }

    public void clickPositionDateOpenYearDropdownUpperYear() {
        inputDateOpenYearOptions.get(1).click();
    }

    public void clickPositionDateOpenDate(int date) {
        click(getDriver().findElement(By.xpath("//div[@aria-label='day-" + date + "'][not(contains(@class,'outside'))]")));
    }

    public void fillPositionDateOpen(int month, int date, int year) {
        if (month != 1) {
            click(inputDateOpenMonthDropdown);
            inputDateOpenMonthOptions.get(month - 2).click();
        }
        int currYear = Year.now().getValue();
        if (year != currYear) {
            click(inputDateOpenYearDropdown);
            int numsOfYearsOptionsDisplayed = inputDateOpenYearOptions.size() - 2;
            if ( year < currYear ) {
                if ( year < currYear - numsOfYearsOptionsDisplayed / 2) {
                    for (int i = 1; i <= currYear - year - numsOfYearsOptionsDisplayed / 2; i++) {
                        clickPositionDateOpenYearDropdownArrowDown();
                    }
                    clickPositionDateOpenYearDropdownBottomYear();
                } else {
                    click(inputDateOpenYearOptions.get(currYear - year + numsOfYearsOptionsDisplayed / 2));
                }
            } else {
                if ( year > currYear + numsOfYearsOptionsDisplayed / 2) {
                    for (int i = 1; i <= year - currYear - numsOfYearsOptionsDisplayed / 2; i++) {
                        clickPositionDateOpenYearDropdownArrowUp();
                    }
                    clickPositionDateOpenYearDropdownUpperYear();
                } else {
                    click(inputDateOpenYearOptions.get(numsOfYearsOptionsDisplayed / 2 - (year - currYear) + 1));
                }

            }
        }
        clickPositionDateOpenDate(date);
    }

}
