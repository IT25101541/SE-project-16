package com.creativepulse.service;

import com.creativepulse.model.Payment;
import com.creativepulse.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository repo;

    public PaymentService(PaymentRepository repo) { this.repo = repo; }

    public List<Payment> findByInvoice(Long invoiceId) { return repo.findByInvoiceId(invoiceId); }

    public Payment findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id " + id));
    }

    public Payment save(Payment payment) { return repo.save(payment); }

    public void delete(Long id) { repo.deleteById(id); }
}
