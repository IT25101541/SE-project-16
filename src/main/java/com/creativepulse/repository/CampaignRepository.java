package com.creativepulse.repository;

import com.creativepulse.entity.Campaign;
import com.creativepulse.entity.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    // Find campaigns that are not archived, ordered by deadline
    List<Campaign> findByArchivedFalseOrderByDeadlineAsc();

    // Count campaigns that are not archived
    long countByArchivedFalse();

    // Count campaigns by status where they are not archived
    long countByStatusAndArchivedFalse(CampaignStatus status);
}