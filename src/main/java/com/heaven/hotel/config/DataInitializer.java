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
                "USR1001|admin|admin@heaven.local|+94-11-555-0100|Admin|User|"
                + hash + "|ADMIN|ACTIVE|" + now + "||" + now + "|ADMIN|1||false|true|true|true");
        FileWriterUtil.addRecord(usersFile,
                "USR1002|staff|staff@heaven.local|+94-11-555-0101|Staff|Member|"
                + hash + "|STAFF|ACTIVE|" + now + "||" + now + "|STAFF|Reception|Staff|||50000|ACTIVE");
        FileWriterUtil.addRecord(usersFile,
                "USR1003|guest|guest@heaven.local|+94-77-555-0102|John|Guest|"
                + hash + "|GUEST|ACTIVE|" + now + "||" + now + "|GUEST|||||STANDARD|0");
        FileWriterUtil.addRecord(usersFile,
                "USR1004|jane|jane@heaven.local|+94-77-555-0103|Jane|Doe|"
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

        // BUDGET tier  (< LKR 8,000/night)
        FileWriterUtil.addRecord(roomsFile, "RM101|101|STANDARD|1|5500.0|AVAILABLE|Cosy single room perfect for solo travellers — steps from Jaffna Clock Tower|true|true|false|false|false|" + isoNow + "|||STANDARD|false");
        FileWriterUtil.addRecord(roomsFile, "RM102|102|STANDARD|2|7000.0|AVAILABLE|Budget twin room with breezy courtyard view — near Jaffna Market|true|true|true|false|false|" + isoNow + "|||STANDARD|true");

        // STANDARD tier  (LKR 8,000–19,999/night)
        FileWriterUtil.addRecord(roomsFile, "RM201|201|STANDARD|2|10000.0|AVAILABLE|Comfortable lagoon-view room with warm Tamil heritage decor|true|true|true|false|false|" + isoNow + "|||STANDARD|true");
        FileWriterUtil.addRecord(roomsFile, "RM202|202|STANDARD|2|14500.0|AVAILABLE|Deluxe garden-view room overlooking palmyra palms and hotel pool|true|true|true|false|true|" + isoNow + "|||STANDARD|true");
        FileWriterUtil.addRecord(roomsFile, "RM203|203|STANDARD|3|18000.0|AVAILABLE|Spacious family room with extra bed — near Nallur Kovil|true|true|true|false|false|" + isoNow + "|||STANDARD|true");

        // LUXURY tier  (LKR 20,000–49,999/night)
        FileWriterUtil.addRecord(roomsFile, "RM301|301|SUITE|4|28000.0|AVAILABLE|Junior suite with separate lounge and sweeping Jaffna Lagoon view|true|true|true|true|true|" + isoNow + "|||SUITE|2|2|false|true|1.5");
        FileWriterUtil.addRecord(roomsFile, "RM302|302|SUITE|4|40000.0|AVAILABLE|Luxury suite with private Jacuzzi and panoramic view of Jaffna Fort|true|true|true|true|true|" + isoNow + "|||SUITE|2|2|true|true|1.5");

        // PRESIDENTIAL tier  (LKR 50,000+/night)
        FileWriterUtil.addRecord(roomsFile, "RM401|401|SUITE|6|65000.0|AVAILABLE|Presidential suite — 3 bedrooms, private pool deck overlooking the Jaffna Lagoon|true|true|true|true|true|" + isoNow + "|||SUITE|3|3|true|true|1.8");
        FileWriterUtil.addRecord(roomsFile, "RM402|402|SUITE|6|95000.0|AVAILABLE|Royal Jaffna Penthouse — rooftop terrace, butler service and Tamil heritage decor|true|true|true|true|true|" + isoNow + "|||SUITE|3|3|true|true|2.0");

        log.info("Demo rooms initialized with LKR pricing: 2 budget, 3 standard, 2 luxury, 2 presidential");
    }
}
