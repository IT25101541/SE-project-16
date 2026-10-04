package com.creativepulse.usermanagement.service;

import com.creativepulse.usermanagement.dto.AdminUserForm;
import com.creativepulse.usermanagement.dto.ChangePasswordForm;
import com.creativepulse.usermanagement.dto.ProfileForm;
import com.creativepulse.usermanagement.dto.RegistrationForm;
import com.creativepulse.usermanagement.model.Role;
import com.creativepulse.usermanagement.model.User;
import com.creativepulse.usermanagement.model.UserStatus;
import com.creativepulse.usermanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Transactional
public class UserService {

    private static final String LETTERS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
    private static final String DIGITS = "23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------------------------------------------------------- sign up

    /** Public self-registration: always creates a CLIENT account. */
    public User registerClient(RegistrationForm form) {
        return createUser(form.getFullName(), form.getEmail(), form.getPhone(),
                form.getPassword(), Role.CLIENT, false);
    }

    /** UM01: an administrator registers a user, picks a role and a temporary password is generated. */
    public CreatedUser createByAdmin(AdminUserForm form) {
        String temporaryPassword = generateTemporaryPassword();
        User user = createUser(form.getFullName(), form.getEmail(), form.getPhone(),
                temporaryPassword, form.getRole(), true);
        return new CreatedUser(user, temporaryPassword);
    }

    private User createUser(String fullName, String email, String phone,
                            String rawPassword, Role role, boolean mustChangePassword) {
        String normalizedEmail = normalizeEmail(email);
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateEmailException("An account with this email already exists");
        }
        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(normalizedEmail);
        user.setPhone(blankToNull(phone));
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);
        user.setMustChangePassword(mustChangePassword);
        return userRepository.save(user);
    }

    // ------------------------------------------------------------------ login

    public void recordLogin(String email) {
        userRepository.findByEmail(normalizeEmail(email)).ifPresent(u -> u.setLastLoginAt(LocalDateTime.now()));
    }

    // ----------------------------------------------------------------- lookup

    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<User> search(String query) {
        if (query == null || query.isBlank()) {
            return userRepository.findAllByOrderByCreatedAtDesc();
        }
        return userRepository.search(query.trim());
    }

    // ------------------------------------------------------ admin management

    public void changeRole(Long id, Role role, String actingEmail) {
        User user = getById(id);
        guardSelf(user, actingEmail, "change your own role");
        user.setRole(role);
    }

    public void setStatus(Long id, UserStatus status, String actingEmail) {
        User user = getById(id);
        guardSelf(user, actingEmail, "suspend your own account");
        user.setStatus(status);
    }

    public void delete(Long id, String actingEmail) {
        User user = getById(id);
        guardSelf(user, actingEmail, "delete your own account");
        userRepository.delete(user);
    }

    private void guardSelf(User target, String actingEmail, String action) {
        if (target.getEmail().equalsIgnoreCase(actingEmail)) {
            throw new IllegalStateException("You cannot " + action);
        }
    }

    // ----------------------------------------------------------------- profile

    public void updateProfile(String email, ProfileForm form) {
        User user = getByEmail(email);
        user.setFullName(form.getFullName().trim());
        user.setPhone(blankToNull(form.getPhone()));
    }

    public void changePassword(String email, ChangePasswordForm form) {
        User user = getByEmail(email);
        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
        user.setMustChangePassword(false);
    }

    // --------------------------------------------------------------- dashboard

    @Transactional(readOnly = true)
    public DashboardStats getDashboardStats() {
        Map<Role, Long> byRole = new EnumMap<>(Role.class);
        for (Role role : Role.values()) {
            byRole.put(role, userRepository.countByRole(role));
        }
        return new DashboardStats(
                userRepository.count(),
                userRepository.countByStatus(UserStatus.ACTIVE),
                userRepository.countByStatus(UserStatus.SUSPENDED),
                userRepository.countByCreatedAtAfter(LocalDateTime.now().minusDays(7)),
                byRole,
                userRepository.findTop5ByOrderByCreatedAtDesc());
    }

    // ----------------------------------------------------------------- helpers

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    /** 10 characters, always contains at least one letter and one digit. */
    static String generateTemporaryPassword() {
        String all = LETTERS + DIGITS;
        char[] chars = new char[10];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = all.charAt(RANDOM.nextInt(all.length()));
        }
        chars[0] = LETTERS.charAt(RANDOM.nextInt(LETTERS.length()));
        chars[1] = DIGITS.charAt(RANDOM.nextInt(DIGITS.length()));
        return new String(chars);
    }
}
