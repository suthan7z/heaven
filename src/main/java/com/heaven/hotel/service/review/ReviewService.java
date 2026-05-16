package com.heaven.hotel.service.review;

import com.heaven.hotel.model.review.Review;
import com.heaven.hotel.utils.IdGenerator;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final List<Review> reviews = new ArrayList<>();

    public Review createReview(String bookingId, String guestId, String roomId,
                               int rating, String title, String comment) {
        Review review = new Review(IdGenerator.generateReviewId(), bookingId, guestId,
                roomId, rating, title, comment);
        reviews.add(review);
        return review;
    }

    public List<Review> getAllReviews() { return new ArrayList<>(reviews); }

    public List<Review> getApprovedReviews() {
        return reviews.stream().filter(Review::isApproved).toList();
    }

    public List<Review> getReviewsByGuest(String guestId) {
        return reviews.stream().filter(r -> r.getGuestId().equals(guestId)).toList();
    }

    public Optional<Review> findById(String reviewId) {
        return reviews.stream().filter(r -> r.getReviewId().equals(reviewId)).findFirst();
    }

    public void approveReview(String reviewId) {
        findById(reviewId).ifPresent(Review::approve);
    }

    public void rejectReview(String reviewId) {
        findById(reviewId).ifPresent(Review::reject);
    }

    public double getAverageRating() {
        return reviews.stream().filter(Review::isApproved)
                .mapToInt(Review::getRating).average().orElse(0);
    }
}
