package com.creativepulse.service.impl;

import com.creativepulse.dto.InvoiceDto;
import com.creativepulse.dto.PaymentDto;
import com.creativepulse.entity.Campaign;
import com.creativepulse.entity.Invoice;
import com.creativepulse.entity.Payment;
import com.creativepulse.entity.User;
import com.creativepulse.entity.enums.InvoiceStatus;
import com.creativepulse.entity.enums.PaymentStatus;
import com.creativepulse.repository.CampaignRepository;
import com.creativepulse.repository.InvoiceRepository;
import com.creativepulse.repository.PaymentRepository;
import com.creativepulse.repository.UserRepository;
import com.creativepulse.service.BillingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    public BillingServiceImpl(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository, CampaignRepository campaignRepository, UserRepository userRepository) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDto> getAllInvoices() {
        return invoiceRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDto> getClientInvoices(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return invoiceRepository.findAll().stream()
                .filter(i -> i.getCampaign().getClient().getUser() != null && i.getCampaign().getClient().getUser().getId().equals(user.getId()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDto createInvoice(InvoiceDto dto) {
        Campaign campaign = campaignRepository.findById(dto.getCampaignId())
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));

        Invoice invoice = new Invoice();
        invoice.setCampaign(campaign);
        invoice.setClient(campaign.getClient());
        invoice.setAmount(dto.getAmount());
        invoice.setTaxAmount(BigDecimal.ZERO);
        invoice.setTotalAmount(dto.getAmount());
        invoice.setDueDate(dto.getDueDate());
        invoice.setIssueDate(LocalDate.now());
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setStatus(InvoiceStatus.ISSUED);
        
        return mapToDto(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentsForInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();
        return invoice.getPayments().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public PaymentDto recordPayment(PaymentDto dto) {
        Invoice invoice = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setTransactionReference(dto.getTransactionReference());
        payment.setPaymentReference("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        
        payment = paymentRepository.save(payment);

        // Update Invoice status
        BigDecimal totalPaid = invoice.getPayments().stream()
                .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPaid.compareTo(invoice.getAmount()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }
        invoiceRepository.save(invoice);

        return mapToDto(payment);
    }

    @Override
    public void updateInvoiceStatus(Long invoiceId, InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        invoice.setStatus(status);
        invoiceRepository.save(invoice);
    }

    private InvoiceDto mapToDto(Invoice inv) {
        InvoiceDto dto = new InvoiceDto();
        dto.setId(inv.getId());
        dto.setAmount(inv.getAmount());
        dto.setDueDate(inv.getDueDate());
        dto.setStatus(inv.getStatus());
        dto.setCampaignId(inv.getCampaign().getId());
        dto.setCampaignName(inv.getCampaign().getName());
        dto.setClientName(inv.getCampaign().getClient().getCompanyName());
        dto.setCreatedAt(inv.getCreatedAt());
        
        BigDecimal paid = inv.getPayments().stream()
                .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setAmountPaid(paid);
        return dto;
    }

    private PaymentDto mapToDto(Payment pay) {
        PaymentDto dto = new PaymentDto();
        dto.setId(pay.getId());
        dto.setAmount(pay.getAmount());
        dto.setPaymentMethod(pay.getPaymentMethod());
        dto.setTransactionReference(pay.getTransactionReference());
        dto.setStatus(pay.getStatus());
        dto.setPaymentDate(pay.getPaymentDate());
        dto.setInvoiceId(pay.getInvoice().getId());
        return dto;
    }
}
