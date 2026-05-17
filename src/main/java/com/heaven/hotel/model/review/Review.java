package com.heaven.hotel.model.review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Guest review for a completed stay.
 * OOP: Encapsulation, Polymorphism (display() overridden by subclasses)
 */
public class Review {

    private String reviewId;
    private String bookingId;
    private String guestId;
    private String roomId;
    private int    rating;    // 1–5 stars
    private String comment;
    private String date;      // YYYY-MM-DD
    private String status;    // "Pending","Approved","Rejected"

    // extra field kept for internal use
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Review() {}

    public Review(String reviewId, String bookingId, String guestId, String roomId,
                  int rating, String comment, String date, String status) {
        this.reviewId  = reviewId;
        this.bookingId = bookingId;
        this.guestId   = guestId;
        this.roomId    = roomId;
        setRating(rating);
        this.comment   = comment;
        this.date      = date;
        this.status    = status;
        this.createdAt = LocalDateTime.now();
    }

    // ── Core operations ───────────────────────────────────────────────────────

    /**
     * Sets status to Pending and saves the review (persistence via service layer).
     */
    public void submit() {
        this.status    = "Pending";
        this.date      = LocalDateTime.now().format(DATE_FMT);
        this.createdAt = LocalDateTime.now();
        System.out.println("Review " + reviewId + " submitted — awaiting moderation");
    }

    public void approve() {
        this.status    = "Approved";
        this.updatedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status    = "Rejected";
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Polymorphic display — subclasses override to add badges or hide identity.
     */
    public void display() {
        System.out.println("Review  : " + reviewId + " | Room: " + roomId);
        System.out.println("Rating  : " + "★".repeat(rating) + "☆".repeat(5 - rating));
        System.out.println("Comment : " + comment);
        System.out.println("Date    : " + date + " | Status: " + status);
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: type|reviewId|bookingId|guestId|roomId|rating|comment|date|status
     */
    @Override
    public String toString() {
        return "Review|" + reviewId + "|" + bookingId + "|" + guestId + "|" + roomId
                + "|" + rating + "|" + nvl(comment) + "|" + nvl(date) + "|" + nvl(status);
    }

    public static Review fromString(String line) {
        String[] f = line.split("\\|", -1);
        int offset = f[0].equals("Review") || f[0].equals("Verified") || f[0].equals("Public") ? 1 : 0;
        Review r = new Review();
        if (f.length > offset)   r.setReviewId(f[offset]);
        if (f.length > offset+1) r.setBookingId(f[offset+1]);
        if (f.length > offset+2) r.setGuestId(f[offset+2]);
        if (f.length > offset+3) r.setRoomId(f[offset+3]);
        if (f.length > offset+4) { try { r.setRating(Integer.parseInt(f[offset+4])); } catch (Exception ignored) {} }
        if (f.length > offset+5) r.setComment(f[offset+5]);
        if (f.length > offset+6) r.setDate(f[offset+6]);
        if (f.length > offset+7) r.setStatus(f[offset+7]);
        return r;
    }

    protected static String nvl(String s) { return s != null ? s : ""; }

    // ── Status helpers ────────────────────────────────────────────────────────

    public boolean isPending()  { return "Pending".equalsIgnoreCase(status); }
    public boolean isApproved() { return "Approved".equalsIgnoreCase(status); }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getReviewId()               { return reviewId; }
    public void   setReviewId(String id)      { this.reviewId = id; }

    public String getBookingId()              { return bookingId; }
    public void   setBookingId(String id)     { this.bookingId = id; }

    public String getGuestId()               { return guestId; }
    public void   setGuestId(String id)      { this.guestId = id; }

    public String getRoomId()                { return roomId; }
    public void   setRoomId(String id)       { this.roomId = id; }

    public int    getRating()                { return rating; }
    public void   setRating(int r)           { this.rating = Math.max(1, Math.min(5, r)); }

    public String getComment()               { return comment; }
    public void   setComment(String c)       { this.comment = c; }

    public String getDate()                  { return date; }
    public void   setDate(String d)          { this.date = d; }

    public String getStatus()               { return status; }
    public void   setStatus(String s)       { this.status = s; }

    public String getTitle()                { return title; }
    public void   setTitle(String t)        { this.title = t; }

    public LocalDateTime getCreatedAt()     { return createdAt; }
    public void setCreatedAt(LocalDateTime v){ this.createdAt = v; }

    public LocalDateTime getUpdatedAt()     { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v){ this.updatedAt = v; }
}
