package com.creativepulse.repository;

import com.creativepulse.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByEmail(String email);
    List<Client> findByCompanyNameContainingIgnoreCaseOrContactPersonContainingIgnoreCase(String a, String b);
}
