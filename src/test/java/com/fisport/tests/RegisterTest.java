package com.fisport.tests;

import com.fisport.pages.RegisterPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Register Tests for Fisport")
public class RegisterTest extends BaseTest{

    static WebDriverWait wait;
    static RegisterPage registerPage;

    @BeforeAll
    static void initPage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Thêm WebDriverWait
        registerPage = new RegisterPage(driver);
    }

    @Test
    @Order(1)
    @DisplayName("Should register successfully with valid credentials")
    void testRegisterSuccess() {
        registerPage.navigate();
        long timestamp = System.currentTimeMillis();
        String randomUsername = "testuser" + timestamp;
        String randomEmail = "testuser" + timestamp + "@fisport.com";
        registerPage.enterUsername(randomUsername);
        registerPage.enterPassword("123456");
        registerPage.enterConfirmPassword("123456");
        registerPage.enterEmail(randomEmail);
        String randomDigits = String.valueOf(timestamp).substring(String.valueOf(timestamp).length() - 8);
        String randomPhone = "09" + randomDigits;
        registerPage.enterPhone(randomPhone);
        registerPage.enterBirthday("2004-01-09");
        registerPage.selectGender("MALE");

        registerPage.clickRegister();

        try {
            By successLocator = registerPage.getGlobalSuccessLocator();
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(successLocator));

            assertTrue(successMessage.isDisplayed(), "Success message did not appear.");
            assertTrue(successMessage.getText().contains("thành công"), "Success message text is incorrect.");

        } catch (Exception e) {
            // Nếu không tìm thấy message thành công -> test fail
            assertTrue(false, "Registration failed. Could not find success message.");
        }

    }

    @ParameterizedTest(name = "CSV Test {index}: User={0}, Expects={7}")
    @Order(2)
    @CsvFileSource(resources = "/register-data.csv", numLinesToSkip = 1, useHeadersInDisplayName = false, encoding = "UTF-8")
    void testRegisterFromCSV(String username, String password, String confirmPassword,
                             String email, String phone, String birthday, String gender,
                             String expectedResult, String expectedMessage) {

        registerPage.navigate();

        username = (username == null) ? "" : username.trim();
        password = (password == null) ? "" : password.trim();
        confirmPassword = (confirmPassword == null) ? "" : confirmPassword.trim();
        email = (email == null) ? "" : email.trim();
        phone = (phone == null) ? "" : phone.trim();
        birthday = (birthday == null) ? "" : birthday.trim();
        gender = (gender == null) ? "" : gender.trim();

        registerPage.enterUsername(username);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterEmail(email);
        registerPage.enterPhone(phone);

        if (!birthday.isEmpty()) {
            registerPage.enterBirthday(birthday);
        }

        if (!gender.isEmpty()) {
            registerPage.selectGender(gender);
        }

        registerPage.clickRegister();

        try {
            if ("error_validation".equals(expectedResult)) {

                By validationErrorLocator = By.xpath(
                        "//div[contains(@class, 'text-danger') and contains(normalize-space(.), '" + expectedMessage + "')]"
                );

                wait.until(ExpectedConditions.visibilityOfElementLocated(validationErrorLocator));
                assertTrue(true, "Validation error found as expected: " + expectedMessage);

            } else if ("error_global".equals(expectedResult)) {

                By globalErrorLocator = registerPage.getGlobalErrorLocator(); // (div.alert-danger)
                WebElement errorBox = wait.until(ExpectedConditions.visibilityOfElementLocated(globalErrorLocator));

                assertTrue(errorBox.getText().contains(expectedMessage),
                        "Global error text mismatch. Expected: '" + expectedMessage + "' but got: '" + errorBox.getText() + "'");

            } else {
                By successLocator = registerPage.getGlobalSuccessLocator();
                wait.until(ExpectedConditions.visibilityOfElementLocated(successLocator));
                assertTrue(true, "CSV success case passed.");
            }

        } catch (Exception e) {
            assertTrue(false, "Test failed: Expected '" + expectedResult + "' but no message was found.");
        }
    }
}
