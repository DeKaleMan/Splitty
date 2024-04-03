package server.api;
import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.service.EventService;

import java.util.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.*;

public class EventControllerTest {

    EventController ctrl;
    TestEventRepository eventRepository;
    EventDTO event1 = new EventDTO("test1", new Date(10, 10, 2005), "owner1", "desc1");
    EventDTO event2 = new EventDTO("test2", new Date(10, 10, 2005), "owner2", "desc2");
    EventDTO event3 = new EventDTO("test3", new Date(10, 10, 2005), "owner3", "desc3");

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        ctrl = new EventController(new EventService(eventRepository));
        event1.id = 1;
        event2.id = 2;
        event3.id = 3;
    }

    // testing whether it works if one entry is null
    @Test
    void nullSave() {
        assertEquals(BAD_REQUEST, ctrl.saveEvent(null).getStatusCode());
    }
    @Test
    void nullSaveName() {
        event1.setName(null);
        assertEquals(BAD_REQUEST, ctrl.saveEvent(event1).getStatusCode());
    }
    @Test
    void nullSaveDate() {
        event1.setDate(null);
        assertEquals(BAD_REQUEST, ctrl.saveEvent(event1).getStatusCode());
    }
    @Test
    void nullSaveDescription() {
        event1.setDescription(null);
        assertEquals(BAD_REQUEST, ctrl.saveEvent(event1).getStatusCode());
    }
    @Test
    void nullSaveOwner() {
        event1.setOwner(null);
        assertEquals(BAD_REQUEST, ctrl.saveEvent(event1).getStatusCode());
    }
    @Test
    void emptyOwner() {
        event1.setOwner("");
        assertEquals(BAD_REQUEST, ctrl.saveEvent(event1).getStatusCode());
    }

    @Test
    void emptyName() {
        event1.setName("");
        assertEquals(BAD_REQUEST, ctrl.saveEvent(event1).getStatusCode());
    }
    @Test
    void testSave() {
        var actual = ctrl.saveEvent(event1);
        // assert whether the saveEvent method returned the right event
        Event newEvent = new Event(event1.getName(), event1.getDate(),
                event1.getOwner(), event1.getDescription());
        assertEquals(newEvent, actual.getBody());
        // assert if the save method was called in the repository
        assertEquals("save", eventRepository.methods.getLast());
        assertEquals(newEvent, eventRepository.events.getLast());
    }
    @Test
    void testFindById() {
        Event actualEvent1 = ctrl.saveEvent(event1).getBody();
        Event actualEvent2 = ctrl.saveEvent(event2).getBody();
        Event actualEvent3 = ctrl.saveEvent(event3).getBody();

        Optional<Event> newEvent1 = eventRepository.findById(actualEvent1.id);
        if (newEvent1.isEmpty()) {
            assertEquals("Test failed because no event was returned", "");
        }
        assertEquals("findById", eventRepository.methods.getLast());
        assertEquals(actualEvent1, newEvent1.get());
    }

    @Test
    void testFindAll() {
        Event actualEvent1 = ctrl.saveEvent(event1).getBody();
        Event actualEvent2 = ctrl.saveEvent(event2).getBody();
        Event actualEvent3 = ctrl.saveEvent(event3).getBody();
        List<Event> events = Arrays.asList(actualEvent1, actualEvent2, actualEvent3);
        assertArrayEquals(events.toArray(), eventRepository.findAll().toArray());
        assertEquals("findAll", eventRepository.methods.getLast());
    }


}
