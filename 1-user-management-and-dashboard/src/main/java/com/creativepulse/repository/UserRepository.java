package com.creativepulse.repository;

import com.creativepulse.model.Role;
import com.creativepulse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/** DAO / Repository pattern - Spring generates the implementation at runtime. */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByFullNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(String a, String b);
    long countByRole(Role role);
}
