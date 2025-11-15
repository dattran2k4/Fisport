package com.fisport.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RegisterPage extends BasePage {

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void navigate() {
        String url = "http://localhost:8080/register";
        navigateTo(url);
    }

    private By usernameField = By.id("username");

    private By passwordField = By.id("password");

    private By confirmPasswordField = By.id("confirmPassword");

    private By emailField = By.id("email");

    private By phoneField = By.id("phone");

    private By birthdayField = By.id("birthday");

    private By maleGenderRadio = By.id("male-gender");

    private By femaleGenderRadio = By.id("female-gender");

    private By registerButton = By.id("register-btn");

    private By globalError = By.cssSelector("div.alert-danger");

    private By globalSuccess = By.cssSelector("div.alert-success");

    public void enterUsername(String username) {
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        type(confirmPasswordField, confirmPassword);
    }

    public void enterEmail(String email) {
        type(emailField, email);
    }

    public void enterPhone(String phone) {
        type(phoneField, phone);
    }

    public void enterBirthday(String yyyyMmDd) {

        try {
            WebElement birthdayInput = this.driver.findElement(birthdayField);
            JavascriptExecutor js = (JavascriptExecutor) this.driver;

            js.executeScript("arguments[0].value = arguments[1];", birthdayInput, yyyyMmDd);
            js.executeScript("arguments[0].dispatchEvent(new Event('change'))", birthdayInput);

        } catch (Exception e) {
            System.err.println("Không thể gán ngày sinh (birthday) bằng JavaScript.");
            e.printStackTrace();
        }
    }

    public void selectGender(String gender) {
        if ("MALE".equalsIgnoreCase(gender)) {
            click(maleGenderRadio);
        } else if ("FEMALE".equalsIgnoreCase(gender)) {
            click(femaleGenderRadio);
        }
    }

    public void clickRegister() {
        click(registerButton);
    }

    public void registerNewUser(String username, String pass, String email, String phone, String dob, String gender) {
        enterUsername(username);
        enterPassword(pass);
        enterConfirmPassword(pass); // Giả sử mật khẩu xác nhận luôn đúng
        enterEmail(email);
        enterPhone(phone);
        enterBirthday(dob); // YYYY-MM-DD
        selectGender(gender); // "MALE" or "FEMALE"
        clickRegister();
    }

    public By getGlobalErrorLocator() {
        return globalError;
    }

    public By getGlobalSuccessLocator() {
        return globalSuccess;
    }
}
