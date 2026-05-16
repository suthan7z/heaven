package com.heaven.hotel.config;

import com.heaven.hotel.filehandler.FileWriterUtil;
import com.heaven.hotel.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private static final String DEMO_PASSWORD = "Demo@1234";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FilePathConfig filePathConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDemoData() {
        String usersFile = filePathConfig.getUsersFilePath();
        String hash = passwordEncoder.encode(DEMO_PASSWORD);
        String now = DateUtil.formatDateTime(LocalDateTime.now());

        FileWriterUtil.clearFile(usersFile);
        FileWriterUtil.addRecord(usersFile,
                "USR1001|admin|admin@heaven.local|+1-555-0100|Admin|User|"
                + hash + "|ADMIN|ACTIVE|" + now + "||" + now + "|ADMIN|1||false|true|true|true");
        FileWriterUtil.addRecord(usersFile,
                "USR1002|staff|staff@heaven.local|+1-555-0101|Staff|Member|"
                + hash + "|STAFF|ACTIVE|" + now + "||" + now + "|STAFF|Reception|Staff|||50000|ACTIVE");
        FileWriterUtil.addRecord(usersFile,
                "USR1003|guest|guest@heaven.local|+1-555-0102|John|Guest|"
                + hash + "|GUEST|ACTIVE|" + now + "||" + now + "|GUEST|||||STANDARD|0");
        FileWriterUtil.addRecord(usersFile,
                "USR1004|jane|jane@heaven.local|+1-555-0103|Jane|Doe|"
                + hash + "|GUEST|ACTIVE|" + now + "||" + now + "|GUEST|||||STANDARD|0");

        log.info("=================================================");
        log.info("Demo users ready. Password for all accounts: {}", DEMO_PASSWORD);
        log.info("  admin / {} -> ADMIN dashboard", DEMO_PASSWORD);
        log.info("  staff / {} -> STAFF dashboard", DEMO_PASSWORD);
        log.info("  guest / {} -> GUEST dashboard", DEMO_PASSWORD);
        log.info("=================================================");

        initializeDemoRooms();
    }

    private void initializeDemoRooms() {
        String roomsFile = filePathConfig.getRoomsFilePath();
        String isoNow = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        FileWriterUtil.clearFile(roomsFile);
        // Format: roomId|roomNumber|roomType|capacity|price|status|description|hasAC|hasWifi|hasTV|hasKitchen|hasBalcony|createdAt|lastMaint|maintNotes|type|extra
        FileWriterUtil.addRecord(roomsFile, "RM101|101|STANDARD|2|120.0|AVAILABLE|Comfortable standard room with garden view|true|true|true|false|false|" + isoNow + "|||STANDARD|true");
        FileWriterUtil.addRecord(roomsFile, "RM102|102|STANDARD|2|120.0|AVAILABLE|Standard room with pool view|true|true|true|false|true|" + isoNow + "|||STANDARD|true");
        FileWriterUtil.addRecord(roomsFile, "RM201|201|STANDARD|2|150.0|AVAILABLE|Deluxe standard room|true|true|true|false|true|" + isoNow + "|||STANDARD|true");
        FileWriterUtil.addRecord(roomsFile, "RM301|301|SUITE|4|350.0|AVAILABLE|Luxury suite with ocean view|true|true|true|true|true|" + isoNow + "|||SUITE|2|2|true|true|1.5");
        FileWriterUtil.addRecord(roomsFile, "RM302|302|SUITE|4|400.0|AVAILABLE|Presidential suite|true|true|true|true|true|" + isoNow + "|||SUITE|3|3|true|true|1.8");

        log.info("Demo rooms initialized: 3 standard rooms, 2 suites");
    }
}
