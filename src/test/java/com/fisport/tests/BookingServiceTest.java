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

public class BookingServiceTest extends  BaseTest {

    static WebDriverWait wait;
    static LoginPage loginPage;
    static BookingPage bookingPage;
    static HomePage homePage;
    static FieldDetailPage fieldDetailPage;

    private static final String FIELD_NAME_TO_CLICK = "Sân bóng đá 2";
    private static final String SUBFIELD_ID_TO_BOOK = "3";
    private static final String START_TIME = "19:00";
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
        loginPage.navigate();
        loginPage.login("johndoe", "123456");
        homePage.waitForPageLoad();

        homePage.clickFieldLink(FIELD_NAME_TO_CLICK);

        try {
            fieldDetailPage.waitForPageLoad();
            fieldDetailPage.clickGoToBookingButton();

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("#bookingForm button[type='submit']")
            ));
        } catch (Exception e) {
            assertTrue(false, "Không thể đến trang đặt sân (Form) từ trang chi tiết.");
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should book a slot successfully (Lần 1)")
    void testBookingSuccess() {

        System.out.println("Chạy Test 1: Đặt thành công...");
        System.out.println("Dữ liệu: Ngày=" + BOOKING_DATE + ", Sân=" + SUBFIELD_ID_TO_BOOK + ", Giờ=" + START_TIME);

        bookingPage.selectDay(BOOKING_DATE);
        bookingPage.selectSubField(SUBFIELD_ID_TO_BOOK);
        bookingPage.selectHour(START_TIME);
        bookingPage.selectDuration(DURATION_MINUTES);

        bookingPage.clickBookButton();

        try {
            wait.until(ExpectedConditions.urlContains("/thanh-toan"));
            assertTrue(driver.getCurrentUrl().contains("/thanh-toan"),
                    "Redirect to payment page");

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Booking was expected to SUCCEED but it FAILED.");
        }
    }

    @Test
    @Order(2)
    @DisplayName("Should verify the booked slot is disabled")
    void testUI_SlotIsDisabledAfterBooking() {


        bookingPage.selectDay(BOOKING_DATE);
        bookingPage.selectSubField(SUBFIELD_ID_TO_BOOK);

        try {
            assertTrue(bookingPage.isHourDisabled(START_TIME),
                    "Khung giờ " + START_TIME + " đáng lẽ phải bị disable sau khi đã book.");

        } catch (Exception e) {
            assertTrue(false, "Test FAILED: Khung giờ đã book nhưng nút vẫn enabled.");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Should verify the booked slot is disabled")
    void testUI_DurationIsDisplayedAfterBooking() {

        bookingPage.selectDay("17/11");
        bookingPage.selectSubField(SUBFIELD_ID_TO_BOOK);
        bookingPage.selectHour("17:30");

        try {
            assertTrue(bookingPage.isDuartionNoShowed("60"),
                    "Duration 60 minutes đáng lẽ phải bị ẩn sau khi đã book.");

        } catch (Exception e) {
            assertTrue(false, "Test FAILED: Khung giờ đã book nhưng nút vẫn hiện duration.");
        }
    }
}
