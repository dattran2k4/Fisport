package com.fisport.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class FieldDetailPage extends BasePage {

    private WebDriver driver;

    public FieldDetailPage(WebDriver driver) {
        super(driver);
    }

    private By goToBookingButton = By.id("btn-booking");

    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(goToBookingButton));
    }

    public void clickGoToBookingButton() {
        click(goToBookingButton);
    }
}
