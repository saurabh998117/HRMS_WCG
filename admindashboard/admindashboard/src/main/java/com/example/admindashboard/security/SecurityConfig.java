package com.example.admindashboard.security;

import com.example.admindashboard.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // 1. Inject your new DB Service

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for development simplicity

                // 2. Define Who Can Access What
                .authorizeHttpRequests(auth -> auth
                        // Public Access (Login page, CSS, Images)
                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()

                        // Role-Based Access (Must match DB roles)
                        .requestMatchers("/admin/**").hasRole("ADMIN")     // Checks for ROLE_ADMIN
                        .requestMatchers("/client/**").hasRole("CLIENT")   // Checks for ROLE_CLIENT
                        .requestMatchers("/employee/**").hasRole("USER")   // Checks for ROLE_USER

                        // All other requests require login
                        .anyRequest().authenticated()
                )

                // 3. Setup Standard Login Form
                .formLogin(form -> form
                        .loginPage("/login")             // The URL of your login page
                        .loginProcessingUrl("/perform_login") // Spring listens for POST requests here automatically
                        .defaultSuccessUrl("/default-redirect", true) // Where to go after success (Traffic Cop)
                        .failureUrl("/login?error=true") // Where to go if password is wrong
                        .permitAll()
                )

                // 4. Connect the Database Service
                .userDetailsService(customUserDetailsService)

                // 5. Logout Handling
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}