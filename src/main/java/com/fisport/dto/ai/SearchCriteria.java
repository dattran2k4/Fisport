package com.fisport.dto.ai;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.LocalDate;
import java.time.LocalTime;

public record SearchCriteria(
        @JsonPropertyDescription("Môn thể thao, ví dụ: cầu lông, bóng đá, tennis")
        String fieldType,

        @JsonPropertyDescription("Tên Quận/huyện, ví dụ: Hải Châu, Sơn Trà, Cẩm Lệ")
        String ward,

        @JsonPropertyDescription("Ngày mà người dùng muốn chơi. Phải ở định dạng YYYY-MM-DD.")
        LocalDate date,

        @JsonPropertyDescription("Thời gian người dùng muốn chơi. Phải ở định dạng HH:mm (24h).")
        LocalTime time,

        @JsonPropertyDescription(
                "Một câu hỏi ngắn gọn, lịch sự mà bot cần hỏi người dùng " +
                        "để lấy thông tin còn thiếu. " +
                        "Nếu tất cả thông tin đã đủ, TRƯỜNG NÀY PHẢI LÀ NULL.")
        String followUpQuestion
) {
    public boolean isReadyForSearch() {
        return (followUpQuestion == null || followUpQuestion.isBlank()) &&
                fieldType != null &&
                ward != null &&
                date != null &&
                time != null;
    }
}
