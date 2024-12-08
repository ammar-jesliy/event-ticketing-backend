/**
 * This class represents an Event in the event ticketing backend system.
 * It is part of the model package and is used to define the properties and behaviors of an event.
 * 
 * Attributes include:
 * - id (unique identifier)
 * - name (name of the event)
 * - description (description of the event)
 * - date (date and time of the event)
 * - openDate (date and time when ticket sales open)
 * - closeDate (date and time when ticket sales close)
 * - location (venue of the event)
 * - maxCapacity (maximum number of tickets the event can sell)
 * - ticketPoolId (unique identifier of the ticket pool associated with the 
 * event)
 * 
 * 
 */
package lk.ac.iit.eventticketingbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Event {
    @Id
    @JsonProperty("id")
    private String id;
    private String name;
    private String description;
    private LocalDateTime date;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private String location;
    private int maxCapacity;
    private String ticketPoolId;

    public Event() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getTicketPoolId() {
        return ticketPoolId;
    }

    public void setTicketPoolId(String ticketPoolId) {
        this.ticketPoolId = ticketPoolId;
    }

    public LocalDateTime getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDateTime openDate) {
        this.openDate = openDate;
    }

    public LocalDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDateTime closeDate) {
        this.closeDate = closeDate;
    }
}
