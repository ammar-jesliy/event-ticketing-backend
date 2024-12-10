/**
 * This class represents a Customer in the event ticketing backend system.
 * It is part of the model package and is used to manage customer-related data.
 * 
 * Attributes include:
 * - id (unique identifier)
 * - name 
 * - email (unique identifier)
 * - password
 * - dateCreated
 * - vipPoints (VIP points earned by the customer)
 * 
 * - purchaseRate (rate at which the customer purchases tickets) TRANSIENT
 * - ticketPool (pool of tickets available for purchase) TRANSIENT
 * - tickets (list of tickets purchased by the customer) TRANSIENT
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

import java.util.ArrayList;
import java.util.List;

@Document
public class Customer implements Runnable {
    @Id
    @JsonProperty("id")
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String dateCreated;
    private int vipPoints;
    private double discountRate;

    @Transient
    private int purchaseRate;
    @Transient
    private TicketPool ticketPool;
    @Transient
    private int amount;
    @Transient
    private List<Ticket> tickets = new ArrayList<>();

    public Customer() {
    }

    public Customer(String name, String email, String password, double discountRate, int purchaseRate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.discountRate = discountRate;
        this.purchaseRate = purchaseRate;
        this.vipPoints = 0;
    }

    /**
     * 
     * This method is used to purchase tickets from the ticket pool.
     * 
     * This method is only used in the CLI simulation and is not part of the
     * REST API.
     * 
     * Continuously attempts to purchase tickets from the ticket pool at a
     * specified rate.
     * If no tickets are left for sale, it prints a message and stops
     * attempting to purchase tickets.
     * 
     * This method runs in an infinite loop until there are no tickets left or
     * the thread is interrupted.
     * 
     * @throws RuntimeException if the thread is interrupted during sleep.
     */
    @Override
    public void run() {
        Logger logger = LoggerFactory.getLogger(Customer.class);

        while (true) {
            try {
                tickets.add(ticketPool.removeTicket(id));
            } catch (IllegalStateException e) {
                logger.warn("No Tickets left for sale. Customer Name: {}. Cannot purchase any more tickets.", name);
                System.out.println("No Tickets left for sale");
                break;
            }

            try {
                Thread.sleep(purchaseRate * 1000);
            } catch (InterruptedException e) {
                logger.error("Customer Name: {} was interrupted while releasing tickets: {}", name, e.getMessage(), e);
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getVipPoints() {
        return vipPoints;
    }

    public void setVipPoints(int vipPoints) {
        this.vipPoints = vipPoints;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(int purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return String.format(
                """
                        Customer {
                          id: '%s',
                          name: '%s',
                          email: '%s',
                          password: '%s',
                          dateCreated: '%s',
                          vipPoints: %d,
                          purchaseRate: %d,
                          tickets: %s
                        }""",
                id, name, email, password, dateCreated, vipPoints, purchaseRate, tickets
        );
    }
}
