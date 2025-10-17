package com.Fisport.service;

import com.Fisport.dto.response.UserDashboardResponse;

import java.math.BigDecimal;

public interface UserDashboardService {
    UserDashboardResponse getData(String username);
}
