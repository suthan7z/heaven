package com.heaven.hotel.filehandler;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Utility class for file operations
 */
public class FileManager {
    
    private FileManager() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Get or create a file at the given path
     */
    public static File getOrCreateFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        // Create parent directories if they don't exist
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        // Create the file if it doesn't exist
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return path.toFile();
    }
    
    /**
     * Check if file exists
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Create directory if not exists
     */
    public static void createDirectoryIfNotExists(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
    
    /**
     * Delete file
     */
    public static boolean deleteFile(String filePath) throws IOException {
        return Files.deleteIfExists(Paths.get(filePath));
    }
    
    /**
     * Read all lines from file
     */
    public static List<String> readAllLines(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return Files.readAllLines(Paths.get(filePath));
    }
    
    /**
     * Write lines to file (overwrite)
     */
    public static void writeLines(String filePath, List<String> lines) throws IOException {
        File file = new File(filePath);
        getOrCreateFile(filePath);
        Files.write(Paths.get(filePath), lines);
    }
    
    /**
     * Append line to file
     */
    public static void appendLine(String filePath, String line) throws IOException {
        File file = new File(filePath);
        getOrCreateFile(filePath);
        Files.write(
            Paths.get(filePath),
            (line + System.lineSeparator()).getBytes(),
            StandardOpenOption.APPEND
        );
    }
    
    /**
     * Append multiple lines to file
     */
    public static void appendLines(String filePath, List<String> lines) throws IOException {
        File file = new File(filePath);
        getOrCreateFile(filePath);
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append(System.lineSeparator());
        }
        Files.write(
            Paths.get(filePath),
            sb.toString().getBytes(),
            StandardOpenOption.APPEND
        );
    }
    
    /**
     * Get file size in bytes
     */
    public static long getFileSize(String filePath) throws IOException {
        return Files.size(Paths.get(filePath));
    }
    
    /**
     * Clear file content
     */
    public static void clearFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.write(path, new byte[0]);
        }
    }
    
    /**
     * Get file extension
     */
    public static String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot > 0) ? fileName.substring(lastDot + 1) : "";
    }
    
    /**
     * Get file name without extension
     */
    public static String getFileNameWithoutExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot > 0) ? fileName.substring(0, lastDot) : fileName;
    }
    
    /**
     * Copy file
     */
    public static void copyFile(String source, String destination) throws IOException {
        Files.copy(Paths.get(source), Paths.get(destination), 
                   StandardCopyOption.REPLACE_EXISTING);
    }
    
    /**
     * Rename file
     */
    public static void renameFile(String oldPath, String newPath) throws IOException {
        Files.move(Paths.get(oldPath), Paths.get(newPath), 
                   StandardCopyOption.REPLACE_EXISTING);
    }
}
