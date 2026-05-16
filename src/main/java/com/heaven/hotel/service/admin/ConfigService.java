package com.heaven.hotel.service.admin;

import com.heaven.hotel.model.admin.HotelConfig;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    private HotelConfig config = new HotelConfig();

    public ConfigService() {
        config.setConfigId("CFG001");
        config.setHotelName("Heaven Hotel");
        config.setAddress("123 Paradise Lane");
        config.setPhone("+1-555-0000");
        config.setEmail("info@heavenhotel.com");
    }

    public HotelConfig getConfig() { return config; }

    public void updateConfig(HotelConfig updated) { this.config = updated; }
}
