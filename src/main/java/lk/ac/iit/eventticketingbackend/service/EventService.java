/**
 * This class provides services related to event management.
 * It contains methods to handle various operations such as creating,
 * updating, deleting, and retrieving event details.
 * 
 * It is part of the service layer in the application architecture and is
 * responsible for handling business logic related to events.
 * 
 */
package lk.ac.iit.eventticketingbackend.service;

import lk.ac.iit.eventticketingbackend.model.Event;
import lk.ac.iit.eventticketingbackend.model.TicketPool;
import lk.ac.iit.eventticketingbackend.repository.EventRepository;
import lk.ac.iit.eventticketingbackend.repository.TicketPoolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final TicketPoolRepository ticketPoolRepository;

    public EventService(EventRepository eventRepository, TicketPoolRepository ticketPoolRepository) {
        this.eventRepository = eventRepository;
        this.ticketPoolRepository = ticketPoolRepository;
    }

    /**
     * Retrieves a list of all events.
     *
     * @return a list of Event objects representing all events.
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Adds a new event and creates a corresponding ticket pool for it.
     * 
     * This method performs the following steps:
     * 1. Saves the event to the event repository.
     * 2. Creates a new ticket pool for the event with initial values.
     * 3. Saves the ticket pool to the ticket pool repository.
     * 4. Updates the event with the ticket pool ID and saves it again.
     * 
     * @param event the event to be added
     * @return the saved event with the associated ticket pool ID
     */
    public Event addEvent(Event event) {
        // Save ticket first
        Event savedEvent = eventRepository.save(event);

        // Create new ticket pool for the event
        TicketPool ticketPool = new TicketPool();
        ticketPool.setEventId(savedEvent.getId());
        ticketPool.setMaxTicketCapacity(savedEvent.getMaxCapacity());
        ticketPool.setTotalTickets(0);
        ticketPool.setTicketSold(0);

        TicketPool savedTicketPool = ticketPoolRepository.save(ticketPool);

        savedEvent.setTicketPoolId((savedTicketPool.getId()));
        return eventRepository.save(savedEvent);

    }
}
