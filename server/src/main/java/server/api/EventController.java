package server.api;
import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import java.util.Optional;



@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventRepository eventDB;

    @Autowired
    public EventController(EventRepository eventDB) {
        this.eventDB = eventDB;
    }

    @GetMapping(path = {"","/"})
    public ResponseEntity<Event> getEventById(@RequestParam int id){
        Optional<Event> event = eventDB.findById(id);
        if(event.isPresent()){
            return ResponseEntity.ok(event.get());

        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Event> addEvent(@RequestBody Event inputEvent){
        if(inputEvent.getName() == null){
            return ResponseEntity.badRequest().build();
        }
        Event newEvent = eventDB.save(inputEvent);
        return ResponseEntity.ok(newEvent);
    }

    @DeleteMapping(path = {"","/"})
    public ResponseEntity<Event> removeEntity(@RequestParam Integer id){
        Optional<Event> eventToDelete = eventDB.findById(id);
        if(eventToDelete.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            eventDB.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

//    @GetMapping("/getNameByName/{name}")
//    public ResponseEntity<Event> getEventByName(@RequestParam("name") String name){
//        ResponseEntity<Event> e = eventDB.getEventByName(name);
//        return e;
//    }


}
