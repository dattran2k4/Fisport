package com.fisport.service;

import com.fisport.dto.response.UserDashboardResponse;

public interface UserDashboardService {
    UserDashboardResponse getData(String username);
}
