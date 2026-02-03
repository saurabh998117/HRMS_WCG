package com.example.admindashboard.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login-submit", "/css/**", "/js/**", "/images/**", "/error/**").permitAll() // Public pages
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Only ADMIN role can access
                        .requestMatchers("/client/**").hasRole("CLIENT")
                        .requestMatchers("/employee/**").hasRole("EMPLOYEE")
                        .anyRequest().authenticated()
                )
                // Add our Custom JWT Filter before the standard Spring checks
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // Disable default login page form
                .formLogin(login -> login.disable())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("auth_token") // Auto-delete cookie on logout
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}