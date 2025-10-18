package com.Fisport.service;

import com.Fisport.dto.request.ReviewRequest;
import com.Fisport.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    void addReview(ReviewRequest request, String username, Long bookingId);

    List<ReviewResponse> getReviewsByUser(String username);
}
