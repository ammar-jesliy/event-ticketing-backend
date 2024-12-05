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

    public List<TicketPool> getAllTicketPools() {
        return ticketPoolRepository.findAll();
    }

    public TicketPool getEventTicketPool(String eventId) {
        return ticketPoolRepository.findTicketPoolByEventId(eventId);
    }
}
