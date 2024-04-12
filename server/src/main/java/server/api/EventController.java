package server.api;

import commons.Event;
import commons.dto.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping(path = {"","/"})
    public ResponseEntity<Event> getEventById(@RequestParam int id) {
        Event event = eventService.getEventById(id);
        if (event != null) {
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> allEvents = eventService.getAllEvents();
        return ResponseEntity.ok(allEvents);
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Event> saveEvent(@RequestBody EventDTO inputEvent) {
        Event newEvent = eventService.saveEvent(inputEvent);
        if (newEvent == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(newEvent);
    }

    @DeleteMapping(path = {"","/"})
    public ResponseEntity<Event> removeEvent(@RequestParam Integer id) {
        Event eventToDelete = eventService.removeEvent(id);
        if (eventToDelete == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(eventToDelete);
        }
    }

    @PutMapping("/updateName")
    public ResponseEntity<Event> updateNameEvent(@RequestParam int id, @RequestParam String newName) {
        Event updatedEvent = eventService.updateEventName(id, newName);
        if (updatedEvent == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedEvent);
        }
    }
}
