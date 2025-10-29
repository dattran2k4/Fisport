package com.fisport.common;


import lombok.*;

@Getter
@AllArgsConstructor
public enum EParticipantStatus {
    PENDING("Đợi phản hồi"),
    ACCEPTED("Chấp nhận"),
    REJECTED("Từ chối"),
    NOSHOW("Không xuất hiện"),
    CANCELLED("Huỷ");

    private final String value;
}
