/**
 * This class represents a request to buy a ticket.
 * It is part of the Data Transfer Object (DTO) layer in the event ticketing backend system.
 * 
 * Attributes include:
 * - eventId
 * - customerId
 * - numberOfTickets
 * 
 */
package lk.ac.iit.eventticketingbackend.dto;

public class TicketBuyRequest {
    private String eventId;
    private String customerId;
    private int numberOfTickets;

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

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
}
