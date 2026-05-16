package com.heaven.hotel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration for file paths used throughout the application
 */
@Component
public class FilePathConfig {
    
    @Value("${app.data.path:src/main/resources/data/}")
    private String dataPath;
    
    @Value("${app.file.encoding:UTF-8}")
    private String fileEncoding;
    
    // User management files
    public String getUsersFilePath() {
        return dataPath + "users.txt";
    }
    
    public String getAdminLogsFilePath() {
        return dataPath + "admin_logs.txt";
    }
    
    // Room management files
    public String getRoomsFilePath() {
        return dataPath + "rooms.txt";
    }
    
    public String getMaintenanceFilePath() {
        return dataPath + "maintenance.txt";
    }
    
    // Booking and payment files
    public String getBookingsFilePath() {
        return dataPath + "bookings.txt";
    }
    
    public String getPaymentsFilePath() {
        return dataPath + "payments.txt";
    }
    
    // Reception files
    public String getKeycardsFilePath() {
        return dataPath + "keycards.txt";
    }
    
    // Review and loyalty files
    public String getReviewsFilePath() {
        return dataPath + "reviews.txt";
    }
    
    public String getLoyaltyFilePath() {
        return dataPath + "loyalty.txt";
    }
    
    // Hotel configuration
    public String getHotelConfigFilePath() {
        return dataPath + "hotel_config.txt";
    }
    
    // Getters for configuration values
    public String getDataPath() {
        return dataPath;
    }
    
    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }
    
    public String getFileEncoding() {
        return fileEncoding;
    }
    
    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }
}
