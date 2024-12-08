/**
 * Represents a ticket in the event ticketing system.
 * This class is part of the model layer in the event-ticketing-backend application.
 * It is used to store and manage information related to event tickets.
 * 
 * Attributes include:
 * - id (unique identifier)
 * - ticketNumber (Number given once the ticket is purchased)
 * - eventId (The unique identifier of the event related to the ticket)
 * - customerId (The unique identifier of the customer who purchased the ticket)
 * - vendorId (The unique identifier of the vendor who sold the ticket)
 * - price (The price of the ticket)
 * - isAvailable (Boolean value indicating whether the ticket is available for 
 * purchase)
 * - purchasedDate (The date and time the ticket was purchased)
 * 
 * 
 */
package lk.ac.iit.eventticketingbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Ticket {
    @Id
    private String id;
    private String ticketNumber;
    private String eventId; // Did not use DBRef to reduce memory usage
    private String customerId; // Did not use DBRef to reduce memory usage
    private String vendorId; // Did not use DBRef to reduce memory usage
    private double price;
    private boolean isAvailable;
    private LocalDateTime purchasedDate;

    public Ticket() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public LocalDateTime getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(LocalDateTime purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", eventId='" + eventId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", vendorId='" + vendorId + '\'' +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                ", purchasedDate=" + purchasedDate +
                '}';
    }
}
