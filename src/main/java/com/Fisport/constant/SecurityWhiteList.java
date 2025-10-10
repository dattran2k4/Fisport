package com.Fisport.constant;

import java.util.ArrayList;
import java.util.List;

public class SecurityWhiteList {
    public static final List<String> WHITE_LIST = List.of("/api/auth/**", "/common/**", "/home/**", "/web/**", "/css/**", "/img/**", "/favicon.ico",
            "/{slug}", "/api/v1/wards", "/bong-da/**", "/cau-long/**", "/tennis/**", "/pickleball/**", "/api/v1/sub-fields", "/booking/**",
            "/san-bong-da-1/**",
            "/login/**"); //test
}
