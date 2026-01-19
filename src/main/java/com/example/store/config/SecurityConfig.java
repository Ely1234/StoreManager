package com.example.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // H2 console uses frames; disabling CSRF for simplicity in this take-home exercise.
    http
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/health", "/actuator/info").permitAll()
            .requestMatchers("/h2-console/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  InMemoryUserDetailsManager userDetailsService() {
    // NOTE: For production, use BCrypt + externalized secrets.
    UserDetails admin = User.withDefaultPasswordEncoder()
        .username("admin")
        .password("adminpass")
        .roles("ADMIN")
        .build();

    UserDetails user = User.withDefaultPasswordEncoder()
        .username("user")
        .password("userpass")
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(admin, user);
  }
}
