package com.heaven.hotel.model.review;

import java.time.LocalDateTime;

public class Review {
    private String reviewId;
    private String bookingId;
    private String guestId;
    private String roomId;
    private int rating; // 1-5
    private String title;
    private String comment;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Review() {}
    public Review(String reviewId, String bookingId, String guestId, String roomId, int rating, String title, String comment) {
        this.reviewId = reviewId; this.bookingId = bookingId; this.guestId = guestId;
        this.roomId = roomId; this.rating = rating; this.title = title; this.comment = comment;
        this.status = "PENDING"; this.createdAt = LocalDateTime.now(); this.updatedAt = LocalDateTime.now();
    }

    public String getReviewId() { return reviewId; }
    public void setReviewId(String v) { this.reviewId = v; }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String v) { this.bookingId = v; }
    public String getGuestId() { return guestId; }
    public void setGuestId(String v) { this.guestId = v; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String v) { this.roomId = v; }
    public int getRating() { return rating; }
    public void setRating(int v) { this.rating = v; }
    public String getTitle() { return title; }
    public void setTitle(String v) { this.title = v; }
    public String getComment() { return comment; }
    public void setComment(String v) { this.comment = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }
    public boolean isPending() { return "PENDING".equals(status); }
    public boolean isApproved() { return "APPROVED".equals(status); }
    public void approve() { this.status = "APPROVED"; this.updatedAt = LocalDateTime.now(); }
    public void reject() { this.status = "REJECTED"; this.updatedAt = LocalDateTime.now(); }
}
