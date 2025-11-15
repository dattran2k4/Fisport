package com.fisport.dto.ai;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public record BookingCriteria(
        @JsonPropertyDescription("Tên của sân con (SubField) mà người dùng đã chọn " +
                "từ danh sách (ví dụ: 'Sân 1', 'Sân Vui Vẻ'). " +
                "Trường này là BẮT BUỘC để đặt.")
        String selectedFieldName,

        @JsonPropertyDescription("Thời lượng chơi (tính bằng phút), " +
                "ví dụ: 60, 90, 120. " +
                "Trường này là BẮT BUỘC để đặt.")
        Integer duration,

        @JsonPropertyDescription("Danh sách tên các dịch vụ/item " +
                "mà người dùng muốn thêm (ví dụ: 'nước suối', 'thuê vợt').")
        List<String> serviceItems,

        @JsonPropertyDescription("Mã voucher mà người dùng cung cấp.")
        String voucherCode,

        @JsonPropertyDescription("Nếu người dùng HỦY (ví dụ: 'thôi', 'không đặt nữa'), " +
                "đặt là true.")
        boolean isCancelled,

        @JsonPropertyDescription("Nếu còn thiếu thông tin (selectedFieldName hoặc duration), " +
                "đây là câu hỏi của bot (ví dụ: 'Bạn muốn chơi trong bao lâu?'). " +
                "Nếu đã đủ, trường này phải là NULL.")
        String followUpQuestion
) {
    public boolean isReadyForBooking() {
        return (followUpQuestion == null || followUpQuestion.isBlank()) &&
                !isCancelled &&
                selectedFieldName != null &&
                duration != null;
    }
}
