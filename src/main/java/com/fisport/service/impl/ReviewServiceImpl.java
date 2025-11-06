package com.fisport.service.impl;

import com.fisport.dto.request.ReviewRequest;
import com.fisport.dto.response.ReviewResponse;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Booking;
import com.fisport.model.Review;
import com.fisport.model.User;
import com.fisport.repository.BookingRepository;
import com.fisport.repository.ReviewRepository;
import com.fisport.repository.UserRepository;
import com.fisport.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j(topic = "REVIEW-SERVICE")
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void addReview(ReviewRequest request, String username, Long bookingId) {
        log.info("Adding review for booking id {}", bookingId);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        //Check rating
        if (request.getRating() > 5 || request.getRating() < 0) {
            throw new InvalidParameterException("Invalid rating");
        }

        //to-do check Protif

        Review review = Review.builder()
                .booking(booking)
                .field(booking.getSubfield().getField())
                .comment(request.getComment())
                .rating(request.getRating())
                .user(user)
                .build();

        reviewRepository.save(review);
        log.info("Review has been saved");
    }

    @Override
    public List<ReviewResponse> getReviewsByUser(String username) {
        log.info("Getting reviews for user {}", username);
        User user =  userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Review> reviews = reviewRepository.findByUser(user);

        return reviews.stream().map(review -> ReviewResponse.builder()
                .fieldName(review.getField().getName())
                .fieldBanner(review.getField().getBanner())
                .comment(review.getComment())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .build()).toList();
    }
}
