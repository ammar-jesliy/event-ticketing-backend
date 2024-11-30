package lk.ac.iit.eventticketingbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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
    private Queue<Ticket> tickets;

    public TicketPool() {
        this.tickets = new ConcurrentLinkedQueue<>();
    }

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


    public Queue<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Queue<Ticket> tickets) {
        this.tickets = tickets;
    }
}
