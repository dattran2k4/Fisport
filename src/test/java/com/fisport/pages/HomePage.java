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
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Tạo một Wait CỰC NGẮN (1 giây) chỉ để kiểm tra
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(1));

        int maxScrollAttempts = 15; // Thử cuộn tối đa 15 lần

        for (int i = 0; i < maxScrollAttempts; i++) {
            try {
                // 1. THỬ TÌM: Dùng wait ngắn để kiểm tra element đã hiển thị chưa
                WebElement element = shortWait.until(
                        ExpectedConditions.visibilityOfElementLocated(locator)
                );

                // 2. NẾU THÀNH CÔNG (không bị TimeoutException):
                // Đã tìm thấy element! Giờ cuộn và click
                System.out.println("Đã tìm thấy element: " + fieldName);
                js.executeScript(
                        "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                        element
                );

                // Đợi 1 chút cho animation 'smooth' chạy xong
                try { Thread.sleep(300); } catch (Exception e) {}

                js.executeScript("arguments[0].click();", element);

                return; // THOÁT HÀM (Đã click thành công)

            } catch (TimeoutException e) {
                // 3. NẾU THẤT BẠI (TimeoutException):
                // Element chưa xuất hiện. Cuộn xuống 1 đoạn và thử lại.
                System.out.println("Chưa tìm thấy, đang cuộn xuống... Lần " + (i + 1));
                js.executeScript("window.scrollBy(0, 350);"); // Cuộn xuống 350px

                // Đợi 1 chút cho data/animation load sau khi cuộn
                try { Thread.sleep(200); } catch (Exception e_inner) {}
            } catch (Exception e) {
                // Bắt các lỗi khác (ví dụ: StaleElement)
                System.err.println("Lỗi khác khi cuộn tìm: " + e.getMessage());
                // Thử cuộn lại
                js.executeScript("window.scrollBy(0, 350);");
            }
        }

        // 4. NẾU HẾT VÒNG LẶP:
        // Đã cuộn 15 lần mà không tìm thấy
        System.err.println("Không thể tìm thấy element sau " + maxScrollAttempts + " lần cuộn: " + fieldName);
        throw new RuntimeException("Không tìm thấy '" + fieldName + "' sau khi cuộn hết trang.");
    }
}
