package com.fisport.tests;

import com.fisport.pages.LoginPage;
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
@DisplayName("Login Tests for Fisport")
public class LoginTest extends BaseTest {

    static WebDriverWait wait;
    static LoginPage loginPage;

    private static final By USER_DROPDOWN_TOGGLE = By.id("userDropdown");

    private static final By LOGOUT_BUTTON_LOCATOR = By.id("logout-btn");

    @BeforeAll
    static void initPage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Thêm WebDriverWait
        loginPage = new LoginPage(driver);
    }

    @Test
    @Order(1)
    @DisplayName("Should login successfully with valid credentials")
    void testLoginSuccess() {
        loginPage.navigate();
        loginPage.login("johndoe", "123456");

        try {
            WebElement dropdownToggle = wait.until(ExpectedConditions.visibilityOfElementLocated(USER_DROPDOWN_TOGGLE));

            dropdownToggle.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(LOGOUT_BUTTON_LOCATOR));

            assertTrue(true, "Login successful!");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Login failed. Could not find user dropdown or logout button on home page.");
        }

        assertTrue(true, "Login successful!");
    }

    @ParameterizedTest(name = "CSV Test {index}: User={0}, Pass={1}, Expects={2}")
    @Order(2)
    @CsvFileSource(resources = "/login-data.csv", numLinesToSkip = 1, useHeadersInDisplayName = false)
    void testLoginFromCSV(String username, String password, String expected) {
        loginPage.navigate();
        username = (username == null) ? "" : username.trim();
        password = (password == null) ? "" : password.trim();

        loginPage.login(username, password);
        if ("success".equals(expected)) {

            try {
                // Đợi nút dropdown (biểu tượng user) xuất hiện
                WebElement dropdownToggle = wait.until(ExpectedConditions.visibilityOfElementLocated(USER_DROPDOWN_TOGGLE));
                // Click để mở menu
                dropdownToggle.click();
                // Đợi nút logout xuất hiện bên trong menu
                wait.until(ExpectedConditions.visibilityOfElementLocated(LOGOUT_BUTTON_LOCATOR));

                // Nếu không ném Exception, test thành công
                assertTrue(true, "CSV test (success) passed for user: " + username);
            } catch (Exception e) {
                // Nếu mong đợi 'success' nhưng thất bại (không tìm thấy nút dropdown/logout)
                e.printStackTrace();
                assertTrue(false, "Login was expected to SUCCEED for user: " + username + " but it FAILED.");
            }
        } else {
            try {
                By errorLocator = loginPage.getErrorLocator();
                wait.until(ExpectedConditions.visibilityOfElementLocated(errorLocator));

                // Nếu tìm thấy message lỗi, test thành công
                assertTrue(true, "CSV test (error) passed for user: " + username);
            } catch (Exception e) {
                e.printStackTrace();
                assertTrue(false, "Login was expected to FAIL for user: " + username + " but it SUCCEEDED.");
            }
        }
    }
}
