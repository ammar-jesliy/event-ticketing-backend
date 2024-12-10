/**
 * This class is responsible for handling HTTP requests related to ticket operations.
 * 
 */
package lk.ac.iit.eventticketingbackend.controller;

import lk.ac.iit.eventticketingbackend.model.Ticket;
import lk.ac.iit.eventticketingbackend.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Fetches all tickets.
     *
     * This method handles HTTP GET requests and returns a list of all tickets.
     * It allows cross-origin requests from "http://localhost:4200".
     *
     * @return a list of all tickets
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<Ticket> fetchAllTickets() {
        return ticketService.getAllTickets();
    }

    /**
     * Retrieves the list of tickets associated with a specific vendor.
     *
     * @param vendorId the ID of the vendor whose tickets are to be retrieved
     * @return a ResponseEntity containing the list of tickets if found, or an
     *         error message if no tickets are found or an error occurs
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<?> getVendorTickets(@PathVariable String vendorId) {
        try {
            List<Ticket> tickets = ticketService.getVendorTickets(vendorId);

            if (tickets.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "message", "No tickets found for vendorId: " + vendorId));
            }

            return ResponseEntity.ok().body(tickets);
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "An error occurred while retrieving tickets: " + e.getMessage()));
        }
    }
}
