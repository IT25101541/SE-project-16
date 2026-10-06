package com.creativepulse.service;

import com.creativepulse.model.Invoice;
import com.creativepulse.model.Payment;
import com.creativepulse.pattern.InvoiceNumberGenerator;
import com.creativepulse.repository.InvoiceRepository;
import com.creativepulse.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository repo;
    private final PaymentRepository paymentRepo;

    public InvoiceService(InvoiceRepository repo, PaymentRepository paymentRepo) {
        this.repo = repo;
        this.paymentRepo = paymentRepo;
    }

    public List<Invoice> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return repo.findAll();
        return repo.findByInvoiceNumberContainingIgnoreCase(keyword);
    }

    public List<Invoice> findAll() { return repo.findAll(); }

    public Invoice findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id " + id));
    }

    /** CREATE - the invoice number comes from the Singleton generator. */
    public Invoice create(Invoice invoice) {
        invoice.setInvoiceNumber(InvoiceNumberGenerator.getInstance().next());
        return repo.save(invoice);
    }

    public Invoice update(Long id, Invoice form) {
        Invoice db = findById(id);
        db.setClient(form.getClient());
        db.setCampaign(form.getCampaign());
        db.setIssueDate(form.getIssueDate());
        db.setDueDate(form.getDueDate());
        db.setAmount(form.getAmount());
        db.setTaxPercent(form.getTaxPercent());
        db.setStatus(form.getStatus());
        db.setNotes(form.getNotes());
        return repo.save(db);
    }

    public void delete(Long id) { repo.deleteById(id); }

    @Transactional(readOnly = true)
    public BigDecimal paidAmount(Long invoiceId) {
        return paymentRepo.findByInvoiceId(invoiceId).stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal balance(Invoice invoice) {
        return invoice.getTotal().subtract(paidAmount(invoice.getId()));
    }

    /** Keeps the invoice status in sync with the payments received. */
    public void refreshStatus(Invoice invoice) {
        if (invoice.getStatus() == Invoice.Status.CANCELLED) return;
        BigDecimal paid = paidAmount(invoice.getId());
        if (paid.compareTo(BigDecimal.ZERO) == 0) {
            invoice.setStatus(Invoice.Status.UNPAID);
        } else if (paid.compareTo(invoice.getTotal()) >= 0) {
            invoice.setStatus(Invoice.Status.PAID);
        } else {
            invoice.setStatus(Invoice.Status.PARTIALLY_PAID);
        }
        repo.save(invoice);
    }

    public long count() { return repo.count(); }
    public long countUnpaid() { return repo.countByStatus(Invoice.Status.UNPAID); }
}
