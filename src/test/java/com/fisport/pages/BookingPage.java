package com.fisport.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BookingPage extends BasePage {

    private WebDriver driver;

    public BookingPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("http://localhost:8080/");
    }

    private By getDayButton(String yyyyMMdd) {
        return By.cssSelector("#day-container .day-btn[data-date='" + yyyyMMdd + "']");
    }

    private By customDateInput = By.id("customDate");

    private By getSubFieldRadio(String subFieldId) {
        return By.cssSelector(".subfield-radio[value='" + subFieldId + "']");
    }

    private By getHourButton(String hhMM) {
        return By.cssSelector("#hours-container .hour-btn[data-time='" + hhMM + "']");
    }

    private By getDurationButton(String minutes) {
        return By.cssSelector("#durationContainer .duration-btn[data-minutes='" + minutes + "']");
    }

    private By bookButton = By.cssSelector("#bookingForm button[type='submit']");

    private By globalErrorAlert = By.cssSelector("div.alert.alert-danger");

    public void selectDay(String yyyyMMdd) {
        By dayLocator = getDayButton(yyyyMMdd);
        // Dùng JS click để tránh bị che
        jsClick(dayLocator);
    }

    public void selectSubField(String subFieldId) {
        By radioLocator = getSubFieldRadio(subFieldId);
        jsClick(radioLocator);
    }

    /**
     * Bước 3: Chọn giờ (Sau khi JS đã render)
     * @param hhMM (ví dụ: "09:00")
     */
    public void selectHour(String hhMM) {
        By hourLocator = getHourButton(hhMM);
        // Phải ĐỢI cho JS (renderHours) chạy xong và nút này xuất hiện
        wait.until(ExpectedConditions.elementToBeClickable(hourLocator));
        jsClick(hourLocator);
    }

    /**
     * Bước 4: Chọn thời lượng (Sau khi JS đã render)
     * @param minutes (ví dụ: "60")
     */
    public void selectDuration(String minutes) {
        By durationLocator = getDurationButton(minutes);
        // Phải ĐỢI cho JS (API durations) chạy xong
        wait.until(ExpectedConditions.elementToBeClickable(durationLocator));
        jsClick(durationLocator);

        // Đợi 1 chút cho API tính giá (preview-timingPrice) chạy xong
        // (Cách tốt hơn là đợi text "Giá tạm tính" xuất hiện)
        try {
            Thread.sleep(500); // Đợi 0.5s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clickBookButton() {
        click(bookButton);
    }

    public By getGlobalErrorLocator() {
        return globalErrorAlert;
    }
}
