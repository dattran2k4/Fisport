package com.fisport.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FieldDetailPage extends BasePage {

    private WebDriver driver;

    public FieldDetailPage(WebDriver driver) {
        super(driver);
    }

    private By goToBookingButton = By.id("btn-booking");

    public void waitForPageLoad() {
        waitForVisibility(goToBookingButton);
    }

    public void clickGoToBookingButton() {
        sleep(1000);
        click(goToBookingButton);
    }
}
