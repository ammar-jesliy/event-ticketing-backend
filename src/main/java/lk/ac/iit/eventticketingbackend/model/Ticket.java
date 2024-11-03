package lk.ac.iit.eventticketingbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Ticket {
    @Id
    private String id;
    private String eventName;
    private LocalDateTime eventDate;
    private double price;
    private boolean isAvailable;
    private String vendorId;

    public Ticket(String eventName, LocalDateTime eventDate, double price, boolean isAvailable, String vendorId) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.price = price;
        this.isAvailable = isAvailable;
        this.vendorId = vendorId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
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

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }
}
