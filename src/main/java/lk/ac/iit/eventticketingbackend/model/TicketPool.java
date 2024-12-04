package lk.ac.iit.eventticketingbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;
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
        if (totalTickets < maxTicketCapacity) {
            boolean success = tickets.add(ticket);
            this.totalTickets++;
            this.availableTickets++;

            System.out.println(Thread.currentThread().getName() + " has added a ticket to the pool. Total tickets available in the ticket pool is " + tickets.size());
            return true;
        }
        return false; // Cannot add ticket, capacity is full
    }

    public synchronized Ticket buyTicket(String customerId) {
        if (ticketSold == maxTicketCapacity) {
            throw new IllegalStateException("No tickets available for sale");
        } else if (availableTickets == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        // Sort tickets by price (lowest price first)
        tickets.sort(Comparator.comparingDouble(Ticket::getPrice));

        Ticket ticket = tickets.get(0);

        // Mark ticket as sold and assign it to the customer
        ticket.setAvailable(false);
        ticket.setCustomerId(customerId);

        // Set ticket number
        ticket.setTicketNumber("TICKET - " + ticket.getId());

        // Set purchased data and time to now
        ticket.setPurchasedDate(LocalDateTime.now());

        // Remove ticket and update count variables
        tickets.remove(0);
        availableTickets--;
        ticketSold++;

        System.out.println(Thread.currentThread().getName() + " has bought a ticket from the pool. Total tickets available in the ticket pool is " + tickets.size());

        notifyAll();

        return ticket;
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
