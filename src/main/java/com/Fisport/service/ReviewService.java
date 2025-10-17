package com.Fisport.service;

import com.Fisport.dto.request.ReviewRequest;
import com.Fisport.model.Review;

public interface ReviewService {

    void addReview(ReviewRequest request, String username, Long bookingId);
}
