package com.fisport.tests;

import com.fisport.pages.BookingPage;
import com.fisport.pages.FieldDetailPage;
import com.fisport.pages.HomePage;
import com.fisport.pages.LoginPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingTest extends  BaseTest {

    static WebDriverWait wait;
    static LoginPage loginPage;
    static BookingPage bookingPage;
    static HomePage homePage;
    static FieldDetailPage  fieldDetailPage;

    private static final String FIELD_NAME_TO_CLICK = "Sân bóng đá 2"; // Tên trên trang chủ
    private static final String SUBFIELD_ID_TO_BOOK = "3"; // ID của Sân con 1
    private static final String START_TIME = "09:00";
    private static final String DURATION_MINUTES = "60";

    // Luôn đặt cho 1 ngày trong tương lai (ví dụ: 3 ngày tới)
    private static final String BOOKING_DATE = LocalDate.now().plusDays(3)
            .format(DateTimeFormatter.ISO_LOCAL_DATE); // Format: "YYYY-MM-DD"

    @BeforeAll
    static void initPageAndLogin() {
        // 1. Khởi tạo
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        bookingPage = new BookingPage(driver);
        fieldDetailPage = new FieldDetailPage(driver);

        // 2. ĐĂNG NHẬP
        System.out.println("Bắt đầu @BeforeAll: Đăng nhập...");
        loginPage.navigate();
        loginPage.login("johndoe", "123456"); // (Sửa tài khoản nếu cần)
        homePage.waitForPageLoad(); // Đợi trang chủ load
        System.out.println("Đăng nhập thành công.");

        System.out.println("Điều hướng đến trang đặt sân...");
        homePage.clickFieldLink(FIELD_NAME_TO_CLICK);

        try {
            fieldDetailPage.waitForPageLoad(); // Đợi trang chi tiết load
            fieldDetailPage.clickGoToBookingButton(); // Click "Đặt sân"

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("#bookingForm button[type='submit']")
            ));
            System.out.println("Đã đến trang đặt sân (Form). @BeforeAll hoàn tất.");
        } catch (Exception e) {
            assertTrue(false, "Không thể đến trang đặt sân (Form) từ trang chi tiết.");
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should book a slot successfully (Lần 1)")
    void testBookingSuccess() {
        // (Chúng ta đã ở trang đặt sân từ @BeforeAll)

        System.out.println("Chạy Test 1: Đặt thành công...");
        System.out.println("Dữ liệu: Ngày=" + BOOKING_DATE + ", Sân=" + SUBFIELD_ID_TO_BOOK + ", Giờ=" + START_TIME);

        // --- ĐIỀN FORM (Theo luồng JS) ---
        bookingPage.selectDay(BOOKING_DATE);       // Bước 1
        bookingPage.selectSubField(SUBFIELD_ID_TO_BOOK); // Bước 2
        bookingPage.selectHour(START_TIME);        // Bước 3 (Đợi JS)
        bookingPage.selectDuration(DURATION_MINUTES); // Bước 4 (Đợi JS)

        bookingPage.clickBookButton();             // Bước 5

        // --- KIỂM TRA (ASSERT) ---
        // Giả định: Đặt thành công sẽ redirect về trang Lịch sử (/booking/history)
        try {
            wait.until(ExpectedConditions.urlContains("/thanh-toan"));
            assertTrue(driver.getCurrentUrl().contains("/thanh-toan"),
                    "Không redirect đến trang lịch sử sau khi đặt thành công.");
            System.out.println("Test 1: THÀNH CÔNG.");

        } catch (Exception e) {
            System.out.println("DEBUG (Booking Success): Test failed. Current URL is: " + driver.getCurrentUrl());
            e.printStackTrace();
            assertTrue(false, "Booking was expected to SUCCEED but it FAILED.");
        }
    }
}
