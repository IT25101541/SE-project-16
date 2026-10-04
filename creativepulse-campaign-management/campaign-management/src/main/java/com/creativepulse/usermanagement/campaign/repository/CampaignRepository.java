package com.creativepulse.usermanagement.campaign.repository;

import com.creativepulse.usermanagement.campaign.model.Campaign;
import com.creativepulse.usermanagement.campaign.model.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findAllByOrderByCreatedAtDesc();

    long countByStatus(CampaignStatus status);
}
