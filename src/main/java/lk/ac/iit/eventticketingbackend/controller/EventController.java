package lk.ac.iit.eventticketingbackend.controller;

import lk.ac.iit.eventticketingbackend.model.Event;
import lk.ac.iit.eventticketingbackend.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<Event> fetchAllEvents() {
        return eventService.getAllEvents();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public Event addEvent(@RequestBody Event event) {
        return eventService.addEvent(event);
    }
}
