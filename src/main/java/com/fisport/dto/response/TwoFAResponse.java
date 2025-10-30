package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TwoFAResponse {
    String url;
    String qrCode;
}
