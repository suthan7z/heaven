package com.heaven.hotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Application configuration bean definitions
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
    }
    
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000", "http://localhost:8080")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
