package com.heaven.hotel.filehandler;

import java.io.*;
import java.util.*;

/**
 * Utility class for writing data to files
 */
public class FileWriterUtil {
    
    private FileWriterUtil() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Add a new record to file
     */
    public static boolean addRecord(String filePath, String record) {
        try {
            FileManager.appendLine(filePath, record);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Add multiple records to file
     */
    public static boolean addRecords(String filePath, List<String> records) {
        try {
            FileManager.appendLines(filePath, records);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update a record by ID
     */
    public static boolean updateRecord(String filePath, String recordId, String newRecord) {
        try {
            List<String> records = FileManager.readAllLines(filePath);
            for (int i = 0; i < records.size(); i++) {
                String record = records.get(i);
                if (record.startsWith(recordId + "|") || record.contains("|" + recordId + "|")) {
                    records.set(i, newRecord);
                    FileManager.writeLines(filePath, records);
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating file: " + filePath + " - " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete a record by ID
     */
    public static boolean deleteRecord(String filePath, String recordId) {
        try {
            List<String> records = FileManager.readAllLines(filePath);
            boolean removed = records.removeIf(record -> 
                record.startsWith(recordId + "|") || record.contains("|" + recordId + "|"));
            
            if (removed) {
                FileManager.writeLines(filePath, records);
            }
            return removed;
        } catch (IOException e) {
            System.err.println("Error deleting from file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete records matching a condition
     */
    public static boolean deleteRecordsMatching(String filePath, String searchTerm) {
        try {
            List<String> records = FileManager.readAllLines(filePath);
            boolean removed = records.removeIf(record -> record.contains(searchTerm));
            
            if (removed) {
                FileManager.writeLines(filePath, records);
            }
            return removed;
        } catch (IOException e) {
            System.err.println("Error deleting from file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Clear all records from file
     */
    public static boolean clearFile(String filePath) {
        try {
            FileManager.clearFile(filePath);
            return true;
        } catch (IOException e) {
            System.err.println("Error clearing file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Replace a field in a record
     */
    public static boolean updateField(String filePath, String recordId, int fieldIndex, String newValue) {
        try {
            List<String> records = FileManager.readAllLines(filePath);
            for (int i = 0; i < records.size(); i++) {
                String record = records.get(i);
                if (record.startsWith(recordId + "|") || record.contains("|" + recordId + "|")) {
                    String[] fields = record.split("\\|", -1);
                    if (fieldIndex < fields.length) {
                        fields[fieldIndex] = newValue;
                        String updatedRecord = String.join("|", fields);
                        records.set(i, updatedRecord);
                        FileManager.writeLines(filePath, records);
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating field: " + filePath + " - " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Backup file by creating a copy with timestamp
     */
    public static boolean backupFile(String filePath) {
        try {
            String backupPath = filePath + ".backup." + System.currentTimeMillis();
            FileManager.copyFile(filePath, backupPath);
            return true;
        } catch (IOException e) {
            System.err.println("Error backing up file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Merge records from one file to another (removes duplicates)
     */
    public static boolean mergeFiles(String sourceFile, String destinationFile) {
        try {
            List<String> sourceRecords = FileManager.readAllLines(sourceFile);
            List<String> destRecords = FileManager.readAllLines(destinationFile);
            
            Set<String> uniqueRecords = new LinkedHashSet<>(destRecords);
            uniqueRecords.addAll(sourceRecords);
            
            FileManager.writeLines(destinationFile, new ArrayList<>(uniqueRecords));
            return true;
        } catch (IOException e) {
            System.err.println("Error merging files - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Write records from list to file (overwrite)
     */
    public static boolean writeRecords(String filePath, List<String> records) {
        try {
            FileManager.writeLines(filePath, records);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }
}
