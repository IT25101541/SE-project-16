package com.creativepulse.pattern.user;

import com.creativepulse.model.Role;
import com.creativepulse.model.User;
import org.springframework.stereotype.Component;

/**
 * Concrete Factory for User Management & Dashboard.
 * Compatible with creativepulse 10-3 – does NOT call setNotes()
 * because the User entity has no notes field.
 */
@Component
public class DefaultUserFactory implements UserFactory {

    @Override
    public User createUser(Role role, String fullName, String username,
                           String email, String phone) {
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        user.setActive(true);

        // Role-specific defaults (without using a notes field)
        // You can later expand this to set other role-based defaults
        // such as default dashboard preferences if you add those fields.
        switch (role) {
            case CLIENT, DESIGNER, FINANCE, MANAGER, SALES, ADMIN -> {
                // All roles are valid; defaults already set above.
                // Active = true is the main shared default.
            }
        }
        return user;
    }
}
