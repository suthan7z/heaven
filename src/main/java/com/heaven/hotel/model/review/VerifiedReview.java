package com.heaven.hotel.model.review;

public class VerifiedReview extends Review {
    private String verificationCode;
    private boolean verified;

    public VerifiedReview() { super(); }

    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String v) { this.verificationCode = v; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean v) { this.verified = v; }
}
