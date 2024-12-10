/**
 * This class represents a pool of tickets for an event.
 * It is part of the event ticketing backend system.
 * <p>
 * Attributes include:
 * - id (unique identifier for the ticket pool)
 * - eventId (unique identifier for the event associated with the ticket pool)
 * - maxTicketCapacity (maximum number of tickets that can be added to the pool)
 * - totalTickets (total number of tickets in the pool, sold and unsold)
 * - ticketSold (number of tickets sold)
 * - availableTickets (number of tickets available for sale)
 * - tickets (list of unsold tickets in the pool)
 */

package lk.ac.iit.eventticketingbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Document
public class TicketPool {
    @Id
    @JsonProperty("id")
    private String id;
    private String eventId;
    private int maxTicketCapacity;
    private int totalTickets;
    private int ticketSold;
    private int availableTickets;
    private List<Ticket> tickets;

    public TicketPool() {
        this.tickets = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Adds a ticket to the ticket pool if the total number of tickets is less than
     * the maximum ticket capacity.
     * This method is synchronized to ensure thread safety.
     * This method is used by the vendor to add tickets to the pool.
     *
     * @param ticket the ticket to be added to the pool
     * @return true if the ticket was successfully added, false if the ticket pool
     * is at maximum capacity
     */
    public synchronized boolean addTicket(Ticket ticket) {
        Logger logger = LoggerFactory.getLogger(TicketPool.class);

        // Get current timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        if (totalTickets < maxTicketCapacity) {
            tickets.add(ticket);
            this.totalTickets++;
            this.availableTickets++;

            logger.info("{} has added a ticket to the pool. Total tickets available in the ticket pool: {}",
                    Thread.currentThread().getName(), tickets.size());

            System.out.println(timestamp + ": " + Thread.currentThread().getName()
                    + " has added a ticket to the pool. Total tickets available in the ticket pool is "
                    + tickets.size());
            notifyAll();
            return true;
        }
        logger.warn("{} tried to add a ticket, but the pool capacity is full.", Thread.currentThread().getName());
        return false; // Cannot add ticket, capacity is full
    }

    /**
     * Buys a ticket for a customer from the ticket pool.
     * This method is synchronized to ensure thread safety.
     * This method is used by the customer to buy tickets from the pool.
     * <p>
     * This method is used only for the REST API implementation.
     * <p>
     * If no tickets are available for sale, an IllegalStateException is thrown.
     * The ticket with the lowest price is sold first.
     * The ticket is removed from the pool and marked as sold.
     * The ticket is assigned to the customer and the ticket number is set.
     * The purchased date and time is set to the current date and time.
     * The ticket count variables are updated.
     * A message is printed to the console indicating that a ticket has been bought.
     * The ticket is returned to the customer.
     * The method blocks if no tickets are available for sale.
     *
     * @param customerId the ID of the customer buying the ticket
     * @return the ticket that was bought
     * @throws IllegalStateException if no tickets are available for sale
     */
    public synchronized Ticket buyTicket(String customerId) {
        Logger logger = LoggerFactory.getLogger(TicketPool.class);

        if (ticketSold == maxTicketCapacity) {
            logger.warn("No tickets available for sale. Max ticket capacity reached.");
            throw new IllegalStateException("No tickets available for sale");
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

        logger.info("{} has bought a ticket from the pool. Total tickets available in the pool: {}",
                Thread.currentThread().getName(), tickets.size());


        System.out.println(Thread.currentThread().getName()
                + " has bought a ticket from the pool. Total tickets available in the ticket pool is "
                + tickets.size());

        return ticket;
    }

    /**
     * Removes a ticket from the ticket pool and assigns it to the specified
     * customer.
     * This method is synchronized to ensure thread safety.
     * This method is used by the customer to purchase tickets from the pool.
     * <p>
     * This method is only used in the CLI simulation and is not part of the
     * REST API.
     * <p>
     * If no tickets are available for sale, an IllegalStateException is thrown.
     * The ticket with the lowest price is sold first.
     * The ticket is removed from the pool and marked as sold.
     * The ticket is assigned to the customer and the ticket number is set.
     * The purchased date and time is set to the current date and time.
     * The ticket count variables are updated.
     * A message is printed to the console indicating that a ticket has been bought.
     * The ticket is returned to the customer.
     * The method blocks if no tickets are available for sale.
     *
     * @param customerId the ID of the customer purchasing the ticket
     * @return the ticket that was removed and assigned to the customer
     * @throws IllegalStateException if no tickets are available for sale
     * @throws RuntimeException      if the thread is interrupted while waiting for
     *                               tickets
     */
    public synchronized Ticket removeTicket(String customerId) {

        Logger logger = LoggerFactory.getLogger(TicketPool.class);


        // Get current timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        if (ticketSold == maxTicketCapacity) {
            logger.warn("No tickets available for sale. All tickets have been sold.");
            throw new IllegalStateException("No tickets available for sale");
        }

        while (tickets.isEmpty()) {
            try {
                logger.info("{} is waiting for tickets...", Thread.currentThread().getName());
                System.out.println(timestamp + ": " + Thread.currentThread().getName() + " is waiting for tickets...");
                wait();
            } catch (InterruptedException e) {
                logger.error("Thread {} was interrupted while waiting for tickets: {}",
                        Thread.currentThread().getName(), e.getMessage(), e);
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

        logger.info("{} has bought a ticket from the pool. Total tickets available in the pool: {}",
                Thread.currentThread().getName(), tickets.size());

        System.out.println(timestamp + ": " + Thread.currentThread().getName()
                + " has bought a ticket from the pool. Total tickets available in the ticket pool is "
                + tickets.size());
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
