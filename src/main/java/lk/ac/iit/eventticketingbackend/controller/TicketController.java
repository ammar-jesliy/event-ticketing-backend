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

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<Ticket> fetchAllTickets() {
        return ticketService.getAllTickets();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<?> getVendorTransactions(@PathVariable String vendorId) {
        try {
            List<Ticket> tickets = ticketService.getVendorTickets(vendorId);

            if (tickets.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "message", "No tickets found for vendorId: " + vendorId
                ));
            }

            return ResponseEntity.ok().body(tickets);
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "An error occurred while retrieving tickets: " + e.getMessage()
            ));
        }
    }
}
