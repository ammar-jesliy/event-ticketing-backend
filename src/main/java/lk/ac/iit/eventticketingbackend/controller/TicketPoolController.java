/**
 * This class is part of the controller layer in the event ticketing backend application.
 * It is responsible for handling HTTP requests related to ticket pools.
 * 
 * Package: lk.ac.iit.eventticketingbackend.controller
 * 
 */
package lk.ac.iit.eventticketingbackend.controller;

import lk.ac.iit.eventticketingbackend.model.TicketPool;
import lk.ac.iit.eventticketingbackend.service.TicketPoolService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ticketpools")
public class TicketPoolController {
    private final TicketPoolService ticketPoolService;

    public TicketPoolController(TicketPoolService ticketPoolService) {
        this.ticketPoolService = ticketPoolService;
    }

    /**
     * Fetches all ticket pools.
     *
     * @return a list of all TicketPool objects.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<TicketPool> fetchAllTicketPools() {
        return ticketPoolService.getAllTicketPools();
    }

    /**
     * Fetches the ticket pool for a specific event.
     *
     * @param eventId the ID of the event for which the ticket pool is to be fetched
     * @return the ticket pool associated with the specified event
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{eventId}")
    public TicketPool fetchEventTicketPool(@PathVariable String eventId) {
        return ticketPoolService.getEventTicketPool(eventId);
    }
}
