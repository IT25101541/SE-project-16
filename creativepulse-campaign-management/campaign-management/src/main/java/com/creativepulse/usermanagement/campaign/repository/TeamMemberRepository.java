package com.creativepulse.usermanagement.campaign.repository;

import com.creativepulse.usermanagement.model.Role;
import com.creativepulse.usermanagement.model.User;
import com.creativepulse.usermanagement.model.UserStatus;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Read-only access to the User table from the Campaign module, so the User Management
 * module's own repository does not need to change.
 */
public interface TeamMemberRepository extends Repository<User, Long> {

    List<User> findByRoleInAndStatusOrderByFullNameAsc(Collection<Role> roles, UserStatus status);

    Optional<User> findById(Long id);
}
