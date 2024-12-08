package lk.ac.iit.eventticketingbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public void run() {
        while (true) {
            try {
                tickets.add(ticketPool.removeTicket(id));
            } catch (IllegalStateException e) {
                System.out.println("No Tickets left for sale");
                break;
            }

            try {
                Thread.sleep(purchaseRate * 1000);
            } catch (InterruptedException e) {
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
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", vipPoints=" + vipPoints +
                ", discountRate=" + discountRate +
                ", purchaseRate=" + purchaseRate +
                ", ticketPool=" + ticketPool +
                ", amount=" + amount +
                ", tickets=" + tickets +
                '}';
    }
}
