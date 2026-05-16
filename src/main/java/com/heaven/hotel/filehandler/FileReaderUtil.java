package com.heaven.hotel.filehandler;

import java.io.*;
import java.util.*;

/**
 * Utility class for reading data from files
 */
public class FileReaderUtil {
    
    private FileReaderUtil() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Read all records from a file
     */
    public static List<String> readAllRecords(String filePath) {
        List<String> records = new ArrayList<>();
        try {
            records = FileManager.readAllLines(filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
        }
        return records;
    }
    
    /**
     * Read records filtered by a condition
     */
    public static List<String> readRecordsFiltered(String filePath, String searchTerm) {
        List<String> filtered = new ArrayList<>();
        try {
            List<String> allRecords = FileManager.readAllLines(filePath);
            for (String record : allRecords) {
                if (record.contains(searchTerm)) {
                    filtered.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
        }
        return filtered;
    }
    
    /**
     * Read a specific record by ID
     */
    public static String readRecordById(String filePath, String id) {
        try {
            List<String> records = FileManager.readAllLines(filePath);
            for (String record : records) {
                if (record.startsWith(id + "|") || record.contains("|" + id + "|")) {
                    return record;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get total number of records in file
     */
    public static int getRecordCount(String filePath) {
        try {
            List<String> records = FileManager.readAllLines(filePath);
            return records.size();
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Check if file contains a record with given ID
     */
    public static boolean recordExists(String filePath, String id) {
        try {
            List<String> records = FileManager.readAllLines(filePath);
            for (String record : records) {
                if (record.startsWith(id + "|") || record.contains("|" + id + "|")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Read first N records
     */
    public static List<String> readFirstNRecords(String filePath, int n) {
        List<String> records = new ArrayList<>();
        try {
            List<String> allRecords = FileManager.readAllLines(filePath);
            for (int i = 0; i < Math.min(n, allRecords.size()); i++) {
                records.add(allRecords.get(i));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
        }
        return records;
    }
    
    /**
     * Read last N records
     */
    public static List<String> readLastNRecords(String filePath, int n) {
        List<String> records = new ArrayList<>();
        try {
            List<String> allRecords = FileManager.readAllLines(filePath);
            int startIndex = Math.max(0, allRecords.size() - n);
            records.addAll(allRecords.subList(startIndex, allRecords.size()));
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
        }
        return records;
    }
    
    /**
     * Search records by field value
     */
    public static List<String> searchByField(String filePath, int fieldIndex, String value) {
        List<String> results = new ArrayList<>();
        try {
            List<String> records = FileManager.readAllLines(filePath);
            for (String record : records) {
                String[] fields = record.split("\\|");
                if (fieldIndex < fields.length && fields[fieldIndex].equals(value)) {
                    results.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
        }
        return results;
    }
    
    /**
     * Get paginated results from file
     */
    public static List<String> getPaginatedRecords(String filePath, int pageNumber, int pageSize) {
        List<String> paginatedRecords = new ArrayList<>();
        try {
            List<String> allRecords = FileManager.readAllLines(filePath);
            int startIndex = (pageNumber - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, allRecords.size());
            
            if (startIndex < allRecords.size()) {
                paginatedRecords.addAll(allRecords.subList(startIndex, endIndex));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
        }
        return paginatedRecords;
    }
}
