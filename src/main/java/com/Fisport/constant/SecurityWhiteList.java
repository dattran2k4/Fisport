package com.Fisport.constant;

import java.util.ArrayList;
import java.util.List;

public class SecurityWhiteList {
    public static final List<String> WHITE_LIST = List.of("/api/auth/**", "/common/**", "/web/**", "/web/css/**", "/web/img/**", "/favicon.ico",
            "/api/v1/wards", "/cau-long/**", "/tennis/**", "/bong-ro", "/pickleball/**", "/api/v1/sub-fields", "/booking/**",
            "/login/**", "/2fa/**", "/",
            "/api/v1/bookings/**",
            "/bong-da/**", "/api/v1/users/**", "/web/js/**"); //test
}
