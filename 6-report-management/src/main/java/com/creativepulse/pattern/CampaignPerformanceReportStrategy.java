package com.creativepulse.pattern;

import com.creativepulse.model.Campaign;
import com.creativepulse.model.Report;
import com.creativepulse.repository.CampaignRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CampaignPerformanceReportStrategy implements ReportStrategy {

    private final CampaignRepository campaignRepository;

    public CampaignPerformanceReportStrategy(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public Report.ReportType supports() {
        return Report.ReportType.CAMPAIGN_PERFORMANCE;
    }

    @Override
    public String build(Report report) {
        List<Campaign> campaigns = campaignRepository.findByStartDateBetween(
                report.getPeriodFrom(), report.getPeriodTo());

        StringBuilder sb = new StringBuilder();
        sb.append("Campaign,Client,Status,Progress %,Budget (LKR),Start,End\n");
        BigDecimal totalBudget = BigDecimal.ZERO;
        for (Campaign c : campaigns) {
            sb.append(safe(c.getName())).append(',')
              .append(safe(c.getClient() == null ? "-" : c.getClient().getCompanyName())).append(',')
              .append(c.getStatus()).append(',')
              .append(c.getProgress()).append(',')
              .append(c.getBudget()).append(',')
              .append(c.getStartDate()).append(',')
              .append(c.getEndDate()).append('\n');
            if (c.getBudget() != null) totalBudget = totalBudget.add(c.getBudget());
        }
        sb.append("TOTAL,,,,").append(totalBudget).append(",,\n");
        sb.append("Campaigns in period,").append(campaigns.size()).append("\n");
        return sb.toString();
    }

    private String safe(String value) {
        if (value == null) return "";
        return value.replace(",", " ");
    }
}
