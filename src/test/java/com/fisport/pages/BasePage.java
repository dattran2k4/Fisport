package com.fisport.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Click safely
    protected void click(By locator) {
        waitForVisibility(locator).click();
    }

    // Send keys safely
    protected void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    // Get text safely
    protected String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    // Navigate to a URL
    public void navigateTo(String url) {
        driver.get(url);
    }

    // Check if element is present
    protected boolean isElementVisible(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void jsClick(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            System.out.println("Không thể click bằng JS vào locator: " + locator);
            e.printStackTrace();
            // Thử click bình thường nếu JS thất bại
            click(locator);
        }
    }
}
