package com.creativepulse.pattern;

import com.creativepulse.model.Invoice;
import com.creativepulse.model.Report;
import com.creativepulse.repository.InvoiceRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class FinancialReportStrategy implements ReportStrategy {

    private final InvoiceRepository invoiceRepository;

    public FinancialReportStrategy(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Report.ReportType supports() {
        return Report.ReportType.FINANCIAL;
    }

    @Override
    public String build(Report report) {
        List<Invoice> invoices = invoiceRepository.findByIssueDateBetween(
                report.getPeriodFrom(), report.getPeriodTo());

        StringBuilder sb = new StringBuilder();
        sb.append("Invoice No,Client,Issue Date,Due Date,Amount,Tax %,Total,Status\n");
        BigDecimal invoiced = BigDecimal.ZERO;
        BigDecimal collected = BigDecimal.ZERO;

        for (Invoice i : invoices) {
            sb.append(i.getInvoiceNumber()).append(',')
              .append(safe(i.getClient() == null ? "-" : i.getClient().getCompanyName())).append(',')
              .append(i.getIssueDate()).append(',')
              .append(i.getDueDate()).append(',')
              .append(i.getAmount()).append(',')
              .append(i.getTaxPercent()).append(',')
              .append(i.getTotal()).append(',')
              .append(i.getStatus()).append('\n');
            invoiced = invoiced.add(i.getTotal());
            if (i.getStatus() == Invoice.Status.PAID) collected = collected.add(i.getTotal());
        }
        sb.append("TOTAL INVOICED,").append(invoiced).append('\n');
        sb.append("TOTAL COLLECTED (paid invoices),").append(collected).append('\n');
        sb.append("OUTSTANDING,").append(invoiced.subtract(collected)).append('\n');
        return sb.toString();
    }

    private String safe(String value) {
        if (value == null) return "";
        return value.replace(",", " ");
    }
}
