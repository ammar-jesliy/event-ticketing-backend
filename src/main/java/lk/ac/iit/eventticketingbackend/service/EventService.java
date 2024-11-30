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

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

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
