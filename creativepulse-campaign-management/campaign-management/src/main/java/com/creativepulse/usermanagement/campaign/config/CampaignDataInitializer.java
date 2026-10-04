package com.creativepulse.usermanagement.campaign.config;

import com.creativepulse.usermanagement.campaign.model.Campaign;
import com.creativepulse.usermanagement.campaign.model.CampaignCategory;
import com.creativepulse.usermanagement.campaign.model.CampaignStatus;
import com.creativepulse.usermanagement.campaign.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Adds a few sample campaigns on first start (same switch as the demo users). */
@Component
public class CampaignDataInitializer implements CommandLineRunner {

    private final CampaignRepository campaignRepository;
    private final boolean seedDemoData;

    public CampaignDataInitializer(CampaignRepository campaignRepository,
                                   @Value("${app.seed-demo-users:true}") boolean seedDemoData) {
        this.campaignRepository = campaignRepository;
        this.seedDemoData = seedDemoData;
    }

    @Override
    public void run(String... args) {
        if (!seedDemoData || campaignRepository.count() > 0) {
            return;
        }
        LocalDate today = LocalDate.now();

        campaignRepository.save(sample("Spring Sale Social Push", "Lanka Fashion House",
                CampaignCategory.SOCIAL_MEDIA, CampaignStatus.ACTIVE,
                today.minusDays(10), today.plusDays(20), "450000.00", 40));
        campaignRepository.save(sample("New Outlet Billboard Launch", "Green Leaf Organics",
                CampaignCategory.OUTDOOR, CampaignStatus.PLANNING,
                today.plusDays(5), today.plusDays(45), "1200000.00", 0));
        campaignRepository.save(sample("Festive TV Spot", "Ceylon Tea Traders",
                CampaignCategory.TELEVISION, CampaignStatus.CANCELLED,
                today.minusDays(30), today.minusDays(2), "2500000.00", 15));
    }

    private Campaign sample(String name, String client, CampaignCategory category, CampaignStatus status,
                            LocalDate start, LocalDate end, String budget, int progress) {
        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setClientName(client);
        campaign.setDescription("Sample campaign created for the demo.");
        campaign.setCategory(category);
        campaign.setStatus(status);
        campaign.setStartDate(start);
        campaign.setEndDate(end);
        campaign.setBudget(new BigDecimal(budget));
        campaign.setProgress(progress);
        campaign.setCreatedByName("Demo data");
        return campaign;
    }
}
