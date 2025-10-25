package com.fisport.service;

import com.fisport.dto.request.ReviewRequest;
import com.fisport.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    void addReview(ReviewRequest request, String username, Long bookingId);

    List<ReviewResponse> getReviewsByUser(String username);
}
