package com.creativepulse.repository;

import com.creativepulse.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByTitleContainingIgnoreCase(String title);
    List<Advertisement> findByCampaignId(Long campaignId);
    long countByStatus(Advertisement.Status status);
}
