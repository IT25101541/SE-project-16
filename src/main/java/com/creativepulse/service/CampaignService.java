package com.creativepulse.service;

import com.creativepulse.model.Campaign;
import com.creativepulse.repository.CampaignRepository;
import com.creativepulse.pattern.campaign.CampaignSubject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignService {

    private final CampaignRepository repo;
    private final CampaignSubject campaignSubject;

    public CampaignService(CampaignRepository repo, CampaignSubject campaignSubject) {
        this.repo = repo;
        this.campaignSubject = campaignSubject;
    }

    public List<Campaign> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return repo.findAll();
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    public List<Campaign> findAll() { return repo.findAll(); }

    public Campaign findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found with id " + id));
    }

    public Campaign save(Campaign campaign) {
        Campaign saved = repo.save(campaign);
        campaignSubject.notifyObservers(saved, "CREATED");
        return saved;
    }

    public Campaign update(Long id, Campaign form) {
        Campaign db = findById(id);
        db.setName(form.getName());
        db.setClient(form.getClient());
        db.setManager(form.getManager());
        db.setStartDate(form.getStartDate());
        db.setEndDate(form.getEndDate());
        db.setBudget(form.getBudget());
        db.setStatus(form.getStatus());
        db.setDescription(form.getDescription());
        db.setProgress(form.getProgress());
        Campaign saved = repo.save(db);
        campaignSubject.notifyObservers(saved, "STATUS_CHANGED");
        return saved;
    }

    public void delete(Long id) { repo.deleteById(id); }

    public long count() { return repo.count(); }
    public long countActive() { return repo.countByStatus(Campaign.Status.ACTIVE); }
}
