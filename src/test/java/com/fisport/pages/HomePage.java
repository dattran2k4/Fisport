package com.fisport.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    private By fieldLink(String fieldNameSlug) {
        // Ví dụ: tìm link có href="/san/cau-long/san-cau-long-a"
        // Hoặc đơn giản là tìm bằng partial text
        return By.partialLinkText(fieldNameSlug);
    }

    // Locator của nút User Dropdown (để xác nhận đã login)
    private By userDropdown = By.id("userDropdown");



    /**
     * Chờ cho đến khi trang chủ được load (bằng cách tìm nút user)
     */
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(userDropdown));
    }

    /**
     * Từ trang chủ, click vào một sân để đi đến trang chi tiết
     * @param fieldName Tên sân (ví dụ: "Sân Cầu Lông A")
     */
    public void clickFieldLink(String fieldName) {
        By locator = fieldLink(fieldName);
        scrollDown(700);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        sleep(1000);
        jsClick(locator);
    }
}
