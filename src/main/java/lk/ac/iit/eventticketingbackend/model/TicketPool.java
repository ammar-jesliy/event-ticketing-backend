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
    @DBRef
    private Event event;
    private int totalTickets;
    private int availableTickets;
    private Queue<Ticket> tickets;

    public TicketPool(Event event, int totalTickets) {
        this.event = event;
        this.totalTickets = totalTickets;
        this.tickets = new ConcurrentLinkedQueue<>();
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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
