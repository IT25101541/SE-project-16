package com.creativepulse.usermanagement.campaign.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Turns on @PreAuthorize so the campaign controller can restrict create / edit / delete
 * to certain roles. It is a separate class so the User Management SecurityConfig
 * does not need to change.
 */
@Configuration
@EnableMethodSecurity
public class CampaignSecurityConfig {
}
