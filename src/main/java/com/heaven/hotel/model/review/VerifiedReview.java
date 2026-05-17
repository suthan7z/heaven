package com.heaven.hotel.model.review;

/**
 * Verified review — linked to a confirmed stay in the system.
 * OOP: Inheritance (extends Review), Polymorphism (display override shows badge)
 */
public class VerifiedReview extends Review {

    private boolean stayConfirmed; // true if the booking is confirmed in the system
    private String  verifiedBadge; // badge label shown during display

    public VerifiedReview() {
        super();
        this.verifiedBadge = "✓ Verified Stay";
    }

    public VerifiedReview(String reviewId, String bookingId, String guestId, String roomId,
                          int rating, String comment, String date, String status,
                          boolean stayConfirmed) {
        super(reviewId, bookingId, guestId, roomId, rating, comment, date, status);
        this.stayConfirmed = stayConfirmed;
        this.verifiedBadge = stayConfirmed ? "✓ Verified Stay" : "Unverified";
    }

    // ── Polymorphic display ───────────────────────────────────────────────────

    @Override
    public void display() {
        System.out.println("[" + verifiedBadge + "]");
        super.display();
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: Verified|reviewId|bookingId|guestId|roomId|rating|comment|date|status|stayConfirmed
     */
    @Override
    public String toString() {
        return "Verified|" + getReviewId() + "|" + getBookingId() + "|" + getGuestId()
                + "|" + getRoomId() + "|" + getRating()
                + "|" + nvl(getComment()) + "|" + nvl(getDate())
                + "|" + nvl(getStatus()) + "|" + stayConfirmed;
    }

    public static VerifiedReview fromString(String line) {
        String[] f = line.split("\\|", -1);
        // f[0]="Verified", then same fields as Review
        VerifiedReview r = new VerifiedReview();
        if (f.length > 1) r.setReviewId(f[1]);
        if (f.length > 2) r.setBookingId(f[2]);
        if (f.length > 3) r.setGuestId(f[3]);
        if (f.length > 4) r.setRoomId(f[4]);
        if (f.length > 5) { try { r.setRating(Integer.parseInt(f[5])); } catch (Exception ignored) {} }
        if (f.length > 6) r.setComment(f[6]);
        if (f.length > 7) r.setDate(f[7]);
        if (f.length > 8) r.setStatus(f[8]);
        if (f.length > 9) r.setStayConfirmed(Boolean.parseBoolean(f[9]));
        return r;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public boolean isStayConfirmed()           { return stayConfirmed; }
    public void    setStayConfirmed(boolean v) {
        this.stayConfirmed = v;
        this.verifiedBadge = v ? "✓ Verified Stay" : "Unverified";
    }

    public String getVerifiedBadge()           { return verifiedBadge; }
    public void   setVerifiedBadge(String b)   { this.verifiedBadge = b; }
}
