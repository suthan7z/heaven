package com.heaven.hotel.exception;

/**
 * Exception thrown when a user is not found
 */
public class UserNotFoundException extends Exception {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
