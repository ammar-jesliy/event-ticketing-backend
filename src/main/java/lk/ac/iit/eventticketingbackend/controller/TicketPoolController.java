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

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<TicketPool> fetchAllTicketPools() {
        return ticketPoolService.getAllTicketPools();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{eventId}")
    public TicketPool fetchEventTicketPool(@PathVariable String eventId) {
        return ticketPoolService.getEventTicketPool(eventId);
    }
}
