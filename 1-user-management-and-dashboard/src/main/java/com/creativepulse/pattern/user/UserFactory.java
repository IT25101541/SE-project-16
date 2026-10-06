package com.creativepulse.pattern.user;

import com.creativepulse.model.Role;
import com.creativepulse.model.User;

/**
 * DESIGN PATTERN – FACTORY METHOD (User Management & Dashboard)
 *
 * Creates correctly configured User objects according to the requested role.
 * Keeps role-specific default values in one place instead of scattering
 * switch/if logic inside UserService or the controller.
 *
 * Compatible with creativepulse 10-3 (User model has no notes field).
 */
public interface UserFactory {

    /**
     * Creates a new User with role-specific defaults.
     *
     * @param role     the Role the new user will have
     * @param fullName full name
     * @param username unique username
     * @param email    email address
     * @param phone    phone number
     * @return a fully initialised User ready to be password-hashed and saved
     */
    User createUser(Role role, String fullName, String username, String email, String phone);
}
