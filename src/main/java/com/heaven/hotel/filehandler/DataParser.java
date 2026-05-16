package com.heaven.hotel.filehandler;

import java.util.*;

/**
 * Utility class for parsing delimited data
 */
public class DataParser {
    
    private static final String DELIMITER = "\\|";
    
    private DataParser() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Parse a delimited record into fields
     */
    public static String[] parseRecord(String record) {
        if (record == null || record.isEmpty()) {
            return new String[0];
        }
        return record.split(DELIMITER, -1);
    }
    
    /**
     * Get a specific field from a record
     */
    public static String getField(String record, int fieldIndex) {
        String[] fields = parseRecord(record);
        if (fieldIndex < fields.length) {
            return fields[fieldIndex];
        }
        return null;
    }
    
    /**
     * Get multiple fields from a record
     */
    public static String[] getFields(String record, int... indices) {
        String[] fields = parseRecord(record);
        String[] result = new String[indices.length];
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] < fields.length) {
                result[i] = fields[indices[i]];
            }
        }
        return result;
    }
    
    /**
     * Create a delimited record from array of values
     */
    public static String createRecord(String... values) {
        return String.join("|", values);
    }
    
    /**
     * Create a delimited record from list of values
     */
    public static String createRecord(List<String> values) {
        return String.join("|", values);
    }
    
    /**
     * Replace a field in a record
     */
    public static String setField(String record, int fieldIndex, String newValue) {
        String[] fields = parseRecord(record);
        if (fieldIndex < fields.length) {
            fields[fieldIndex] = newValue != null ? newValue : "";
            return String.join("|", fields);
        }
        return record;
    }
    
    /**
     * Convert record to map with field names as keys
     */
    public static Map<String, String> parseRecordToMap(String record, String... fieldNames) {
        Map<String, String> map = new HashMap<>();
        String[] fields = parseRecord(record);
        
        for (int i = 0; i < fieldNames.length && i < fields.length; i++) {
            map.put(fieldNames[i], fields[i]);
        }
        return map;
    }
    
    /**
     * Create record from map
     */
    public static String createRecordFromMap(Map<String, String> map, String... fieldNames) {
        List<String> values = new ArrayList<>();
        for (String fieldName : fieldNames) {
            values.add(map.getOrDefault(fieldName, ""));
        }
        return String.join("|", values);
    }
    
    /**
     * Parse multiple records into list of maps
     */
    public static List<Map<String, String>> parseRecordsToMaps(List<String> records, String... fieldNames) {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (String record : records) {
            mapList.add(parseRecordToMap(record, fieldNames));
        }
        return mapList;
    }
    
    /**
     * Get field count in a record
     */
    public static int getFieldCount(String record) {
        return parseRecord(record).length;
    }
    
    /**
     * Check if field exists in record
     */
    public static boolean hasField(String record, int fieldIndex) {
        return fieldIndex >= 0 && fieldIndex < getFieldCount(record);
    }
    
    /**
     * Get all fields as list
     */
    public static List<String> parseRecordToList(String record) {
        return Arrays.asList(parseRecord(record));
    }
    
    /**
     * Escape special characters in value
     */
    public static String escapeValue(String value) {
        if (value == null) {
            return "";
        }
        // Escape pipe character and quotes
        return value.replace("|", "\\|").replace("\"", "\\\"");
    }
    
    /**
     * Unescape special characters in value
     */
    public static String unescapeValue(String value) {
        if (value == null) {
            return "";
        }
        // Unescape pipe character and quotes
        return value.replace("\\|", "|").replace("\\\"", "\"");
    }
    
    /**
     * Parse CSV/TSV line safely handling quoted fields
     */
    public static List<String> parseQuotedRecord(String record) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < record.length(); i++) {
            char c = record.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < record.length() && record.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == '|' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString());
        return fields;
    }
}
