package com.creativepulse.service;

import com.creativepulse.dto.InvoiceDto;
import com.creativepulse.dto.PaymentDto;
import com.creativepulse.entity.enums.InvoiceStatus;

import java.util.List;

public interface BillingService {
    List<InvoiceDto> getAllInvoices();
    List<InvoiceDto> getClientInvoices(String username);
    InvoiceDto createInvoice(InvoiceDto invoiceDto);
    
    List<PaymentDto> getPaymentsForInvoice(Long invoiceId);
    PaymentDto recordPayment(PaymentDto paymentDto);
    void updateInvoiceStatus(Long invoiceId, InvoiceStatus status);
}
