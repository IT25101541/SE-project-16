package com.creativepulse.service;

import com.creativepulse.entity.Campaign;
import com.creativepulse.entity.CampaignStatus;
import com.creativepulse.entity.Client;
import com.creativepulse.repository.CampaignRepository;
import com.creativepulse.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final ClientRepository clientRepository;

    public CampaignService(
            CampaignRepository campaignRepository,
            ClientRepository clientRepository) {
        this.campaignRepository = campaignRepository;
        this.clientRepository = clientRepository;
    }

    public List<Campaign> findActive() {
        return campaignRepository.findByArchivedFalseOrderByDeadlineAsc();
    }

    public List<Campaign> findAll() {
        return campaignRepository.findAll();
    }

    public Campaign findById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Campaign not found: " + id));
    }

    public Campaign save(Campaign campaign) {

        // client_id is NOT NULL in the database, so fail clearly before Hibernate.
        if (campaign.getClient() == null || campaign.getClient().getId() == null) {
            throw new IllegalArgumentException(
                    "Please select a client before saving the campaign."
            );
        }

        Long clientId = campaign.getClient().getId();

        // Load the managed Client entity from the database.
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Selected client was not found: " + clientId
                        ));

        // This guarantees Hibernate has a real Client association.
        campaign.setClient(client);

        if (campaign.getStartDate() != null
                && campaign.getDeadline() != null
                && !campaign.getDeadline().isAfter(campaign.getStartDate())) {
            throw new IllegalArgumentException(
                    "Deadline must be after the start date."
            );
        }

        if (campaign.getStatus() == null) {
            campaign.setStatus(CampaignStatus.PLANNED);
        }

        if (campaign.getStatus() == CampaignStatus.COMPLETED) {
            campaign.setProgress(100);
        }

        if (campaign.getProgress() == 100
                && campaign.getStatus() == CampaignStatus.IN_PROGRESS) {
            campaign.setStatus(CampaignStatus.COMPLETED);
        }

        return campaignRepository.save(campaign);
    }

    public void cancel(Long id) {
        Campaign c = findById(id);
        c.setCancelled(true);
        c.setStatus(CampaignStatus.CANCELLED);
        campaignRepository.save(c);
    }

    public void archive(Long id) {
        Campaign c = findById(id);
        c.setArchived(true);
        c.setStatus(CampaignStatus.ARCHIVED);
        campaignRepository.save(c);
    }

    public void delete(Long id) {
        campaignRepository.deleteById(id);
    }

    public long total() {
        return campaignRepository.countByArchivedFalse();
    }

    public long active() {
        return campaignRepository.countByStatusAndArchivedFalse(
                CampaignStatus.IN_PROGRESS
        );
    }

    public long completed() {
        return campaignRepository.countByStatusAndArchivedFalse(
                CampaignStatus.COMPLETED
        );
    }
}
