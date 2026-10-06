package com.creativepulse.repository;

import com.creativepulse.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByInvoiceNumberContainingIgnoreCase(String number);
    List<Invoice> findByStatus(Invoice.Status status);
    List<Invoice> findByClientId(Long clientId);
    List<Invoice> findByIssueDateBetween(LocalDate from, LocalDate to);
    long countByStatus(Invoice.Status status);
}
