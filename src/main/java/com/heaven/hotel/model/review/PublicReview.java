package com.heaven.hotel.model.review;

/**
 * Public review — may be submitted anonymously or via a third-party source.
 * OOP: Inheritance (extends Review), Polymorphism (display hides name when anonymous)
 */
public class PublicReview extends Review {

    private boolean isAnonymous; // hides reviewer name on display
    private String  source;      // "Website","Walk-In","Third-Party"

    public PublicReview() {
        super();
        this.source = "Website";
    }

    public PublicReview(String reviewId, String bookingId, String guestId, String roomId,
                        int rating, String comment, String date, String status,
                        boolean isAnonymous, String source) {
        super(reviewId, bookingId, guestId, roomId, rating, comment, date, status);
        this.isAnonymous = isAnonymous;
        this.source      = source;
    }

    // ── Polymorphic display ───────────────────────────────────────────────────

    @Override
    public void display() {
        System.out.println("[Public Review — " + source + "]");
        System.out.println("Guest   : " + (isAnonymous ? "Anonymous" : getGuestId()));
        System.out.println("Room    : " + getRoomId());
        System.out.println("Rating  : " + "★".repeat(getRating()) + "☆".repeat(5 - getRating()));
        System.out.println("Comment : " + getComment());
        System.out.println("Date    : " + getDate() + " | Status: " + getStatus());
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: Public|reviewId|bookingId|guestId|roomId|rating|comment|date|status|isAnonymous|source
     */
    @Override
    public String toString() {
        return "Public|" + getReviewId() + "|" + getBookingId() + "|" + getGuestId()
                + "|" + getRoomId() + "|" + getRating()
                + "|" + nvl(getComment()) + "|" + nvl(getDate())
                + "|" + nvl(getStatus()) + "|" + isAnonymous + "|" + nvl(source);
    }

    public static PublicReview fromString(String line) {
        String[] f = line.split("\\|", -1);
        PublicReview r = new PublicReview();
        if (f.length > 1) r.setReviewId(f[1]);
        if (f.length > 2) r.setBookingId(f[2]);
        if (f.length > 3) r.setGuestId(f[3]);
        if (f.length > 4) r.setRoomId(f[4]);
        if (f.length > 5) { try { r.setRating(Integer.parseInt(f[5])); } catch (Exception ignored) {} }
        if (f.length > 6) r.setComment(f[6]);
        if (f.length > 7) r.setDate(f[7]);
        if (f.length > 8) r.setStatus(f[8]);
        if (f.length > 9) r.setAnonymous(Boolean.parseBoolean(f[9]));
        if (f.length > 10) r.setSource(f[10]);
        return r;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public boolean isAnonymous()           { return isAnonymous; }
    public void    setAnonymous(boolean v) { this.isAnonymous = v; }

    public String  getSource()             { return source; }
    public void    setSource(String s)     { this.source = s; }

    public String  getDisplayName()        { return isAnonymous ? "Anonymous" : getGuestId(); }
    public void    setDisplayName(String n){ /* identity controlled by isAnonymous flag */ }
}
