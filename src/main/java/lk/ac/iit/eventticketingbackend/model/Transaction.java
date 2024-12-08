/**
 * This class represents a Transaction in the event ticketing backend system.
 * It is part of the model package and is used to manage transaction-related data.
 * 
 * Attributes include:
 * - id (unique identifier)
 * - timeStamp (time at which the transaction occurred)
 * - ticketIds (list of ticket IDs associated with the transaction)
 * - customerId (unique identifier of the customer involved in the transaction)
 * - vendorId (unique identifier of the vendor involved in the transaction)
 * - eventId (unique identifier of the event involved in the transaction)
 * - quantity (number of tickets purchased)
 * - pricePerTicket (price per ticket)
 * - totalAmount (total amount of the transaction)
 * - transactionType (type of transaction, e.g., Purchase, Release)
 * 
 * 
 */
package lk.ac.iit.eventticketingbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
public class Transaction {
    @Id
    private String id;
    private LocalDateTime timeStamp;
    private List<String> ticketIds;
    private String customerId;
    private String vendorId;
    private String eventId;
    private int quantity;
    private double pricePerTicket;
    private double totalAmount;
    private String transactionType;

    public Transaction() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<String> getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(List<String> ticketIds) {
        this.ticketIds = ticketIds;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerTicket() {
        return pricePerTicket;
    }

    public void setPricePerTicket(double pricePerTicket) {
        this.pricePerTicket = pricePerTicket;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
