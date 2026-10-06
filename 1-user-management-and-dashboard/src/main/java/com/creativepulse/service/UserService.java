package com.creativepulse.service;

import com.creativepulse.model.Role;
import com.creativepulse.model.User;
import com.creativepulse.repository.UserRepository;
import com.creativepulse.pattern.user.UserFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final UserFactory userFactory;

    public UserService(UserRepository repo, PasswordEncoder encoder, UserFactory userFactory) {
        this.repo = repo;
        this.encoder = encoder;
        this.userFactory = userFactory;
    }

    public List<User> findAll() { return repo.findAll(); }

    public List<User> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return repo.findAll();
        return repo.findByFullNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(keyword, keyword);
    }

    public User findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
    }

    public List<User> findByRole(Role role) { return repo.findByRole(role); }

    public boolean usernameTaken(String username, Long ignoreId) {
        return repo.findByUsername(username)
                .filter(u -> !u.getId().equals(ignoreId))
                .isPresent();
    }

    public boolean emailTaken(String email, Long ignoreId) {
        return repo.findByEmail(email)
                .filter(u -> !u.getId().equals(ignoreId))
                .isPresent();
    }

    /** CREATE - password is hashed before saving. */
    public User create(User form, String rawPassword) {
        User user = userFactory.createUser(
                form.getRole(),
                form.getFullName(),
                form.getUsername(),
                form.getEmail(),
                form.getPhone());
        user.setPassword(encoder.encode(rawPassword));
        return repo.save(user);
    }

    /** UPDATE - keeps the old password when the field is left empty. */
    public User update(Long id, User form, String rawPassword) {
        User db = findById(id);
        db.setFullName(form.getFullName());
        db.setUsername(form.getUsername());
        db.setEmail(form.getEmail());
        db.setPhone(form.getPhone());
        db.setRole(form.getRole());
        db.setActive(form.isActive());
        if (rawPassword != null && !rawPassword.isBlank()) {
            db.setPassword(encoder.encode(rawPassword));
        }
        return repo.save(db);
    }

    /** DELETE */
    public void delete(Long id) { repo.deleteById(id); }

    public long count() { return repo.count(); }
}
