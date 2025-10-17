package com.Fisport.service.impl;

import com.Fisport.dto.request.ReviewRequest;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.Booking;
import com.Fisport.model.Review;
import com.Fisport.model.User;
import com.Fisport.repository.BookingRepository;
import com.Fisport.repository.ReviewRepository;
import com.Fisport.repository.UserRepository;
import com.Fisport.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void addReview(ReviewRequest request, String username, Long bookingId) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Review review = Review.builder()
                .booking(booking)
                .field(booking.getSubfield().getField())
                .comment(request.getComment())
                .rating(request.getRating())
                .user(user)
                .build();

        reviewRepository.save(review);
    }
}
