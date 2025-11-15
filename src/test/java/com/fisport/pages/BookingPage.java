package com.fisport.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BookingPage extends BasePage {

    private WebDriverWait wait;

    public BookingPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo() {
        navigateTo("http://localhost:8080/");
    }

    private By getDayButton(String yyyyMMdd) {
        return By.xpath("//button[contains(text(),'" + yyyyMMdd + "')]");
    }

    private By customDateInput = By.id("customDate");

    private By getSubFieldRadio(String subFieldId) {
        return By.cssSelector(".subfield-radio[value='" + subFieldId + "']");
    }

    private By getHourButton(String hhMM) {
        return By.xpath("//button[contains(@class,'hour-btn') and text()='" + hhMM + "']");
    }

    private By getDurationButton(String minutes) {
        return By.xpath("//button[contains(@class,'duration-btn') and contains(text(),'" + minutes + "')]");
    }

    private By bookButton = By.cssSelector("#bookingForm button[type='submit']");

    private By globalErrorAlert = By.cssSelector("div.alert.alert-danger");

    public void selectDay(String yyyyMMdd) {
        sleep(500);
        By dayLocator = getDayButton(yyyyMMdd);
        waitForElementClickable(dayLocator);
        jsClick(dayLocator);
    }

    public void selectSubField(String subFieldId) {
        sleep(500);
        By radioLocator = getSubFieldRadio(subFieldId);
        jsClick(radioLocator);
    }

    /**
     * Bước 3: Chọn giờ (Sau khi JS đã render)
     * @param hhMM (ví dụ: "09:00")
     */
    public void selectHour(String hhMM) {
        sleep(500);
        By hourLocator = getHourButton(hhMM);
        wait.until(ExpectedConditions.elementToBeClickable(hourLocator));
        jsClick(hourLocator);
    }

    /**
     * Bước 4: Chọn thời lượng (Sau khi JS đã render)
     * @param minutes (ví dụ: "60")
     */
    public void selectDuration(String minutes) {
        sleep(500);
        scrollDown(700);
        sleep(500);
        By durationLocator = getDurationButton(minutes);
        wait.until(ExpectedConditions.elementToBeClickable(durationLocator));
        jsClick(durationLocator);
    }

    public void clickBookButton() {
        click(bookButton);
    }

    public By getGlobalErrorLocator() {
        return globalErrorAlert;
    }

    public boolean isHourDisabled(String hour) {
        By hourLocator = getHourButton(hour);

        waitForVisibility(hourLocator);

        return !driver.findElement(hourLocator).isEnabled();

    }

    public boolean isDuartionNoShowed(String durationMinutes) {
        By durationLocator = getDurationButton(durationMinutes);

        return wait.until(ExpectedConditions.invisibilityOfElementLocated(durationLocator));
    }
}
