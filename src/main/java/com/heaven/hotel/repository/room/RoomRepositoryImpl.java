package com.heaven.hotel.repository.room;

import com.heaven.hotel.config.FilePathConfig;
import com.heaven.hotel.filehandler.FileReaderUtil;
import com.heaven.hotel.filehandler.FileWriterUtil;
import com.heaven.hotel.model.room.Room;
import com.heaven.hotel.model.room.StandardRoom;
import com.heaven.hotel.model.room.SuiteRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * File-based implementation of RoomRepository
 */
@Repository
public class RoomRepositoryImpl implements RoomRepository {
    
    @Autowired
    private FilePathConfig filePathConfig;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @Override
    public boolean save(Room room) {
        String record = roomToRecord(room);
        return FileWriterUtil.addRecord(filePathConfig.getRoomsFilePath(), record);
    }
    
    @Override
    public boolean update(Room room) {
        List<Room> rooms = findAll();
        
        // Remove old room and add updated one
        rooms.removeIf(r -> r.getRoomId().equals(room.getRoomId()));
        rooms.add(room);
        
        // Rewrite file
        FileWriterUtil.clearFile(filePathConfig.getRoomsFilePath());
        for (Room r : rooms) {
            FileWriterUtil.addRecord(filePathConfig.getRoomsFilePath(), roomToRecord(r));
        }
        
        return true;
    }
    
    @Override
    public boolean delete(String roomId) {
        List<Room> rooms = findAll();
        boolean removed = rooms.removeIf(r -> r.getRoomId().equals(roomId));
        
        if (removed) {
            FileWriterUtil.clearFile(filePathConfig.getRoomsFilePath());
            for (Room r : rooms) {
                FileWriterUtil.addRecord(filePathConfig.getRoomsFilePath(), roomToRecord(r));
            }
        }
        
        return removed;
    }
    
    @Override
    public Optional<Room> findById(String roomId) {
        return findAll().stream()
            .filter(r -> r.getRoomId().equals(roomId))
            .findFirst();
    }
    
    @Override
    public Optional<Room> findByNumber(int roomNumber) {
        return findAll().stream()
            .filter(r -> String.valueOf(roomNumber).equals(r.getRoomNumber()))
            .findFirst();
    }
    
    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        List<String> lines = FileReaderUtil.readAllRecords(filePathConfig.getRoomsFilePath());
        
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            try {
                Room room = recordToRoom(line);
                if (room != null) rooms.add(room);
            } catch (Exception e) {
                System.err.println("Skipping bad room record: " + e.getMessage());
            }
        }
        
        return rooms;
    }
    
    @Override
    public List<Room> findByType(String roomType) {
        return findAll().stream()
            .filter(r -> r.getRoomType().equals(roomType))
            .toList();
    }
    
    @Override
    public List<Room> findByStatus(String status) {
        return findAll().stream()
            .filter(r -> r.getStatus().equals(status))
            .toList();
    }
    
    @Override
    public List<Room> findAvailableRooms() {
        return findByStatus("AVAILABLE");
    }
    
    @Override
    public List<Room> findOccupiedRooms() {
        return findByStatus("OCCUPIED");
    }
    
    @Override
    public List<Room> findMaintenanceRooms() {
        return findByStatus("MAINTENANCE");
    }
    
    @Override
    public int countRooms() {
        return findAll().size();
    }
    
    @Override
    public int countByStatus(String status) {
        return (int) findAll().stream()
            .filter(r -> r.getStatus().equals(status))
            .count();
    }
    
    // Helper methods
    private String roomToRecord(Room room) {
        String type = room instanceof SuiteRoom ? "SUITE" : "STANDARD";
        
        StringBuilder record = new StringBuilder();
        record.append(room.getRoomId()).append("|")
            .append(room.getRoomNumber()).append("|")
            .append(room.getRoomType()).append("|")
            .append(room.getCapacity()).append("|")
            .append(room.getPricePerNight()).append("|")
            .append(room.getStatus()).append("|")
            .append(room.getDescription()).append("|")
            .append(room.isHasAC()).append("|")
            .append(room.isHasWifi()).append("|")
            .append(room.isHasTV()).append("|")
            .append(room.isHasKitchen()).append("|")
            .append(room.isHasBalcony()).append("|")
            .append(room.getCreatedAt() != null ? room.getCreatedAt().format(DATE_FORMATTER) : "").append("|")
            .append(room.getLastMaintenanceDate() != null ? room.getLastMaintenanceDate().format(DATE_FORMATTER) : "").append("|")
            .append(room.getMaintenanceNotes() != null ? room.getMaintenanceNotes() : "").append("|")
            .append(type);
        
        // Type-specific fields
        if (room instanceof SuiteRoom) {
            SuiteRoom suite = (SuiteRoom) room;
            record.append("|").append(suite.getBedroomCount())
                .append("|").append(suite.getBathroomCount())
                .append("|").append(suite.isHasJacuzzi())
                .append("|").append(suite.isHasSeparateLounge())
                .append("|").append(suite.getPremiumMultiplier());
        } else if (room instanceof StandardRoom) {
            StandardRoom standard = (StandardRoom) room;
            record.append("|").append(standard.isHasBathtub());
        }
        
        return record.toString();
    }
    
    private Room recordToRoom(String line) {
        String[] parts = line.split("\\|");
        
        if (parts.length < 16) return null;
        
        String roomId = parts[0];
        int roomNumber = Integer.parseInt(parts[1]);
        int capacity = Integer.parseInt(parts[3]);
        double price = Double.parseDouble(parts[4]);
        String status = parts[5];
        String description = parts[6];
        boolean hasAC = Boolean.parseBoolean(parts[7]);
        boolean hasWifi = Boolean.parseBoolean(parts[8]);
        boolean hasTV = Boolean.parseBoolean(parts[9]);
        boolean hasKitchen = Boolean.parseBoolean(parts[10]);
        boolean hasBalcony = Boolean.parseBoolean(parts[11]);
        LocalDateTime createdAt = parts[12].isEmpty() ? null : LocalDateTime.parse(parts[12], DATE_FORMATTER);
        LocalDateTime lastMaint = parts[13].isEmpty() ? null : LocalDateTime.parse(parts[13], DATE_FORMATTER);
        String maintNotes = parts[14];
        String type = parts[15];
        
        Room room;
        if ("SUITE".equals(type)) {
            SuiteRoom suite = new SuiteRoom();
            suite.setBedroomCount(parts.length > 16 ? Integer.parseInt(parts[16]) : 2);
            suite.setBathroomCount(parts.length > 17 ? Integer.parseInt(parts[17]) : 2);
            suite.setHasJacuzzi(parts.length > 18 ? Boolean.parseBoolean(parts[18]) : true);
            suite.setHasSeparateLounge(parts.length > 19 ? Boolean.parseBoolean(parts[19]) : true);
            suite.setPremiumMultiplier(parts.length > 20 ? Double.parseDouble(parts[20]) : 1.5);
            room = suite;
        } else {
            StandardRoom standard = new StandardRoom();
            standard.setHasBathtub(parts.length > 16 ? Boolean.parseBoolean(parts[16]) : true);
            room = standard;
        }
        
        room.setRoomId(roomId);
        room.setRoomNumber(roomNumber);
        room.setCapacity(capacity);
        room.setPricePerNight(price);
        room.setStatus(status);
        room.setDescription(description);
        room.setHasAC(hasAC);
        room.setHasWifi(hasWifi);
        room.setHasTV(hasTV);
        room.setHasKitchen(hasKitchen);
        room.setHasBalcony(hasBalcony);
        room.setCreatedAt(createdAt);
        room.setLastMaintenanceDate(lastMaint);
        room.setMaintenanceNotes(maintNotes);
        
        return room;
    }
}
