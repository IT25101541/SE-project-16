package com.creativepulse.usermanagement.repository;

import com.creativepulse.usermanagement.model.Role;
import com.creativepulse.usermanagement.model.User;
import com.creativepulse.usermanagement.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByStatus(UserStatus status);

    long countByRole(Role role);

    long countByCreatedAtAfter(LocalDateTime since);

    List<User> findTop5ByOrderByCreatedAtDesc();

    List<User> findAllByOrderByCreatedAtDesc();

    @Query("""
            select u from User u
            where lower(u.fullName) like lower(concat('%', :q, '%'))
               or lower(u.email) like lower(concat('%', :q, '%'))
            order by u.createdAt desc
            """)
    List<User> search(@Param("q") String q);
}
