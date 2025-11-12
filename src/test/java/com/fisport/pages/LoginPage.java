package com.fisport.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;

public class LoginPage extends BasePage {

    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigate() {
        String url = "http://localhost:8080/login";
        navigateTo(url);
    }

    private By usernameField =  By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By errorMsg = By.cssSelector(".error-message");

    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }

    public By getErrorLocator() {
        return errorMsg;
    }
}
