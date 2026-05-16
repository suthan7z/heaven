package com.heaven.hotel.repository.review;

import com.heaven.hotel.model.review.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    boolean save(Review review);
    boolean update(Review review);
    boolean delete(String reviewId);
    Optional<Review> findById(String reviewId);
    List<Review> findAll();
    List<Review> findByGuestId(String guestId);
    List<Review> findByRoomId(String roomId);
    List<Review> findByStatus(String status);
}
