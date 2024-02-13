package com.tus.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // Replace with a database-backed UserDetailsService in a real application
        return username -> User
            .withUsername("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("USER")
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> 
                authorize
                    .antMatchers("/").permitAll() // Allow access to the home page without authentication
                    .anyRequest().authenticated()
            )
            .formLogin(login -> 
                login
                    .loginPage("/login")
                    .permitAll()
            )
            .logout(logout -> 
                logout
                    .permitAll()
            );
    }
}
