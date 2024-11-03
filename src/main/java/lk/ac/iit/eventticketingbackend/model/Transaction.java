package lk.ac.iit.eventticketingbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Transaction {
    @Id
    private String id;
    private String ticketId;
    private String CustomerId;
    private String vendorId;
    private LocalDateTime transactionDate;
    private double amountPaid;
    private String paymentMethod;

    public Transaction(String ticketId, String customerId, String vendorId, LocalDateTime transactionDate, double amountPaid, String paymentMethod) {
        this.ticketId = ticketId;
        CustomerId = customerId;
        this.vendorId = vendorId;
        this.transactionDate = transactionDate;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
