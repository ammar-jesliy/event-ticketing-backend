/**
 * This class is part of the controller layer in the event ticketing backend application.
 * It is responsible for handling HTTP requests related to events.
 */
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

    /**
     * Fetches all events.
     *
     * This method handles HTTP GET requests and returns a list of all events.
     *
     * @return a list of all events
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<Event> fetchAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * Adds a new event to the system.
     *
     * @param event the event to be added
     * @return the added event
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public Event addEvent(@RequestBody Event event) {
        return eventService.addEvent(event);
    }
}
