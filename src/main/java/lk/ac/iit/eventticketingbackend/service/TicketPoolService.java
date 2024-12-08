/**
 * The TicketPoolService class provides services related to managing ticket 
 * pools within the event ticketing backend system.
 * 
 * This service includes functionalities such as creating, updating, and 
 * retrieving ticket pools for various events.
 * 
 * It is part of the service layer in the application architecture, 
 * interacting with the data access layer to perform operations on ticket pool 
 * data.
 * 
 */
package lk.ac.iit.eventticketingbackend.service;

import lk.ac.iit.eventticketingbackend.model.TicketPool;
import lk.ac.iit.eventticketingbackend.repository.TicketPoolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketPoolService {
    private final TicketPoolRepository ticketPoolRepository;

    public TicketPoolService(TicketPoolRepository ticketPoolRepository) {
        this.ticketPoolRepository = ticketPoolRepository;
    }

    /**
     * Retrieves a list of all ticket pools.
     *
     * @return a list of TicketPool objects representing all ticket pools.
     */
    public List<TicketPool> getAllTicketPools() {
        return ticketPoolRepository.findAll();
    }

    /**
     * Retrieves the ticket pool for a specific event.
     *
     * @param eventId the ID of the event for which the ticket pool is to be
     *                retrieved
     * @return the ticket pool associated with the specified event
     */
    public TicketPool getEventTicketPool(String eventId) {
        return ticketPoolRepository.findTicketPoolByEventId(eventId);
    }
}
