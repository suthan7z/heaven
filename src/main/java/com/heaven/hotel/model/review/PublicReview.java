package com.heaven.hotel.model.review;

public class PublicReview extends Review {
    private String displayName;
    private boolean anonymous;

    public PublicReview() { super(); }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String v) { this.displayName = v; }
    public boolean isAnonymous() { return anonymous; }
    public void setAnonymous(boolean v) { this.anonymous = v; }
}
