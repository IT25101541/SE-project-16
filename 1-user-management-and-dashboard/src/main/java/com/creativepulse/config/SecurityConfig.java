package com.creativepulse.config;

import com.creativepulse.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Login, logout and ROLE BASED ACCESS CONTROL.
 * Each module is only reachable by the roles listed in the proposal (Slide 6).
 */
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**", "/videos/**").permitAll()
                .requestMatchers("/users/**").hasRole("ADMIN")
                .requestMatchers("/reports/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/clients/**").hasAnyRole("ADMIN", "SALES")
                .requestMatchers("/campaigns/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/tasks/**").hasAnyRole("ADMIN", "MANAGER", "DESIGNER")
                .requestMatchers("/advertisements/**").hasAnyRole("ADMIN", "DESIGNER", "MANAGER")
                .requestMatchers("/invoices/**", "/payments/**").hasAnyRole("ADMIN", "FINANCE")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));

        return http.build();
    }
}
