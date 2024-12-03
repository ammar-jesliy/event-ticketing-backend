package lk.ac.iit.eventticketingbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Document
public class TicketPool {
    @Id
    @JsonProperty("id")
    private String id;
    private String eventId;
    private int maxTicketCapacity;
    private int totalTickets; // The amount of tickets in the ticket pool, sold and unsold
    private int ticketSold; // The amount of tickets sold
    private int availableTickets; // The amount of tickets available for sale
    private List<Ticket> tickets;

    public TicketPool() {
        this.tickets = Collections.synchronizedList(new ArrayList<>());
    }

    // Add a ticket to the pool (Vendor's operation)
    public synchronized boolean addTicket(Ticket ticket) {
        if (tickets.size() < maxTicketCapacity) {
            tickets.add(ticket);
            return true;
        }
        return false; // Cannot add ticket, capacity is full
    }


    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTicketSold() {
        return ticketSold;
    }

    public void setTicketSold(int ticketSold) {
        this.ticketSold = ticketSold;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }


    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
