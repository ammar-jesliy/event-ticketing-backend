/**
 * This class provides services related to ticket management in the event  
 * ticketing system.
 * 
 * It includes methods for creating, updating, deleting, and retrieving tickets.
 * 
 * It is part of the service layer in the application architecture and is
 * responsible for handling business logic related to tickets.
 * 
 */
package lk.ac.iit.eventticketingbackend.service;

import lk.ac.iit.eventticketingbackend.model.Ticket;
import lk.ac.iit.eventticketingbackend.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Retrieves a list of all tickets.
     *
     * @return a list of Ticket objects representing all tickets.
     */
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    /**
     * Retrieves a list of tickets associated with a specific vendor.
     *
     * @param vendorId the unique identifier of the vendor
     * @return a list of tickets associated with the specified vendor
     */
    public List<Ticket> getVendorTickets(String vendorId) {
        return ticketRepository.findTicketsByVendorId(vendorId);
    }
}
