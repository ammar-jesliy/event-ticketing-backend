/**
 * This class represents a Vendor in the event ticketing backend system.
 * It is part of the model package and is used to manage vendor-related data.
 * 
 * Attributes include:
 * - id (unique identifier)
 * - name 
 * - email (unique identifier)
 * - password
 * - dateCreated
 * 
 * - releaseRate (rate at which tickets are released by the vendor) TRANSIENT
 * - ticketPool (reference to the pool of tickets available) TRANSIENT
 * 
 * 
 */
package lk.ac.iit.eventticketingbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Vendor implements Runnable {
    @Id
    @JsonProperty("id")
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private LocalDateTime dateCreated;

    @Transient
    private int releaseRate;
    @Transient
    private TicketPool ticketPool;
    @Transient
    private int amount;

    public Vendor() {
    }

    public Vendor(String name, String email, String password, int releaseRate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.releaseRate = releaseRate;
    }

    /**
     * 
     * This method is only called in the CLI simulation.
     * 
     * Continuously creates and releases tickets into the ticket pool at a
     * specified rate.
     * Each ticket is assigned a price, vendor ID, and event ID, and is marked
     * as available.
     * If the ticket pool reaches its capacity, the process stops and a message
     * is printed.
     * The thread sleeps for a specified release rate interval between
     * releasing tickets.
     *
     * @throws RuntimeException if the thread is interrupted during sleep
     */
    @Override
    public void run() {
        Logger logger = LoggerFactory.getLogger(Vendor.class);

        while (true) {
            Ticket ticket = new Ticket();
            ticket.setPrice(100);
            ticket.setVendorId(id);
            ticket.setEventId(ticket.getEventId());
            ticket.setAvailable(true);

            boolean success = ticketPool.addTicket(ticket);

            if (!success) {
                logger.warn("Ticket Pool capacity has reached. Vendor Name: {}. Cannot release any more tickets.", name);
                System.out.println("Ticket Pool capacity has reached. Cannot release any more tickets");
                break;
            }

            try {
                Thread.sleep(releaseRate * 1000);
            } catch (InterruptedException e) {
                logger.error("Vendor Name: {} was interrupted while releasing tickets: {}", name, e.getMessage(), e);
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getReleaseRate() {
        return releaseRate;
    }

    public void setReleaseRate(int releaseRate) {
        this.releaseRate = releaseRate;
    }

    public TicketPool getTicketPool() {
        return ticketPool;
    }

    public void setTicketPool(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", releaseRate=" + releaseRate +
                '}';
    }
}
