package com.heaven.hotel.config;

import com.heaven.hotel.service.auth.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the application
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests((authorize) -> authorize
                // Public endpoints
                .requestMatchers("/", "/home", "/about", "/contact").permitAll()
                .requestMatchers("/auth/**", "/login", "/register", "/forgot-password").permitAll()
                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                
                // Admin endpoints
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Staff endpoints
                .requestMatchers("/reception/**").hasAnyRole("STAFF", "ADMIN")
                
                // Guest and Staff endpoints
                .requestMatchers("/bookings/**", "/reviews/**").hasAnyRole("GUEST", "STAFF", "ADMIN")
                
                // Authenticated endpoints
                .requestMatchers("/profile/**", "/dashboard").authenticated()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .rememberMe((remember) -> remember
                .key("heavyhotelSecretKey")
                .tokenValiditySeconds(604800) // 7 days
                .rememberMeParameter("remember-me")
            )
            .csrf((csrf) -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers((headers) -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
            );
        
        return http.build();
    }
}
