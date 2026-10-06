package com.creativepulse.repository;

import com.creativepulse.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByNameContainingIgnoreCase(String name);
    List<Campaign> findByStatus(Campaign.Status status);
    List<Campaign> findByStartDateBetween(LocalDate from, LocalDate to);
    long countByStatus(Campaign.Status status);
    List<Campaign> findByClientId(Long clientId);
}
