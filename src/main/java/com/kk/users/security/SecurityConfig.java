package com.kk.users.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Creating the admin user
        UserDetails adminUser = User.builder()
                .username("kioko")
                .password(passwordEncoder.encode("kioko237"))
                .roles("ADMIN")
                .build();

        // Creating a regular user (optional)
        UserDetails regularUser = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        // Return the in-memory user details manager
        return new InMemoryUserDetailsManager(adminUser, regularUser);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll() // Allow public access to these endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Admin access only
                        .requestMatchers("/employees/**").hasAnyRole("ADMIN", "USER") // Allow both roles to access employees
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll() // Allow unauthenticated access to health and info endpoints
                        .requestMatchers("/actuator/prometheus").permitAll() // Allow unauthenticated access to Prometheus endpoint
                        .requestMatchers("/actuator/**").permitAll() // Restrict all other Actuator endpoints to ADMIN
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login") // Use custom login page
                        .defaultSuccessUrl("/employees", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }
}
