package server.api;
import commons.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventRepository eventDB;

    public EventController(EventRepository eventDB) {
        this.eventDB = eventDB;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Event> getEventById(@PathVariable("id") int id){
        Optional<Event> event = eventDB.findById(id);
        if(event.isPresent()){
            return ResponseEntity.ok(event.get());

        }else{
            return ResponseEntity.badRequest().build();
        }
    }

}
