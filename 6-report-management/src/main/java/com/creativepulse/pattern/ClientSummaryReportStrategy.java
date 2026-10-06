package com.creativepulse.pattern;

import com.creativepulse.model.Client;
import com.creativepulse.model.Report;
import com.creativepulse.repository.CampaignRepository;
import com.creativepulse.repository.ClientRepository;
import com.creativepulse.repository.InvoiceRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientSummaryReportStrategy implements ReportStrategy {

    private final ClientRepository clientRepository;
    private final CampaignRepository campaignRepository;
    private final InvoiceRepository invoiceRepository;

    public ClientSummaryReportStrategy(ClientRepository clientRepository,
                                       CampaignRepository campaignRepository,
                                       InvoiceRepository invoiceRepository) {
        this.clientRepository = clientRepository;
        this.campaignRepository = campaignRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Report.ReportType supports() {
        return Report.ReportType.CLIENT_SUMMARY;
    }

    @Override
    public String build(Report report) {
        List<Client> clients = clientRepository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("Client,Contact Person,Email,Phone,Industry,Active,Campaigns,Invoices\n");
        for (Client c : clients) {
            sb.append(safe(c.getCompanyName())).append(',')
              .append(safe(c.getContactPerson())).append(',')
              .append(safe(c.getEmail())).append(',')
              .append(safe(c.getPhone())).append(',')
              .append(safe(c.getIndustry())).append(',')
              .append(c.isActive() ? "Yes" : "No").append(',')
              .append(campaignRepository.findByClientId(c.getId()).size()).append(',')
              .append(invoiceRepository.findByClientId(c.getId()).size()).append('\n');
        }
        sb.append("Total clients,").append(clients.size()).append("\n");
        return sb.toString();
    }

    private String safe(String value) {
        if (value == null) return "";
        return value.replace(",", " ");
    }
}
