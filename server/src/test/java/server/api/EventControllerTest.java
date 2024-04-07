package server.api;
import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.EventService;

import java.util.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.*;

public class EventControllerTest {

    EventController sut;
    TestEventRepository eventRepository;
    Event event1 = new Event("test1", new Date(10, 10, 2005), "owner1", "desc1");
    Event event2 = new Event("test2", new Date(10, 10, 2005), "owner2", "desc2");
    Event event3 = new Event("test3", new Date(10, 10, 2005), "owner3", "desc3");

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        sut = new EventController(new EventService(eventRepository));
        event1.id = 1;
        event2.id = 2;
        event3.id = 3;
        eventRepository.events.addAll(List.of(event1,event2,event3));
    }

    // testing whether it works if one entry is null
    @Test
    void nullSave() {
        assertEquals(BAD_REQUEST, sut.saveEvent(null).getStatusCode());
    }
    @Test
    void nullSaveName() {
        EventDTO e = new EventDTO(null, new Date(10, 10, 2005), "owner1", "desc1");
        assertEquals(BAD_REQUEST, sut.saveEvent(e).getStatusCode());
    }
    @Test
    void nullSaveDate() {
        EventDTO e = new EventDTO("test", null, "owner1", "desc1");
        assertEquals(BAD_REQUEST, sut.saveEvent(e).getStatusCode());
    }
    @Test
    void nullSaveDescription() {
        EventDTO e = new EventDTO("test", new Date(10, 10, 2005), "owner1", null);
        assertEquals(BAD_REQUEST, sut.saveEvent(e).getStatusCode());
    }
    @Test
    void nullSaveOwner() {
        EventDTO e = new EventDTO("test", new Date(10, 10, 2005), null, "desc1");
        assertEquals(BAD_REQUEST, sut.saveEvent(e).getStatusCode());
    }
    @Test
    void emptyOwner() {
        EventDTO e = new EventDTO("test", new Date(10, 10, 2005), "", "desc1");
        assertEquals(BAD_REQUEST, sut.saveEvent(e).getStatusCode());
    }

    @Test
    void emptyName() {
        EventDTO e = new EventDTO("", new Date(10, 10, 2005), "owner1", "desc1");
        assertEquals(BAD_REQUEST, sut.saveEvent(e).getStatusCode());
    }
    @Test
    void testSave() {
        EventDTO e = new EventDTO("test", new Date(10, 10, 2005), "owner1", "desc1");
        ResponseEntity<Event> response = sut.saveEvent(e);
        assertEquals("save", eventRepository.methods.getLast());
        Event expected = new Event("test", new Date(10, 10, 2005), "owner1", "desc1");
        assertEquals(expected, response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        //cleanup
        eventRepository.events.removeLast();
    }
    @Test
    void testFindById() {
        ResponseEntity<Event> response = sut.getEventById(1);
        assertEquals(event1,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void testFindAll() {
        List<Event> expected = List.of(event1, event2, event3);
        ResponseEntity<List<Event>> response = sut.getAllEvents();
        assertEquals(expected,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("findAll", eventRepository.methods.getLast());
    }

    @Test
    void testDeleteEvent(){
        Event toDelete = new Event("test", new Date(10, 10, 2005), "owner1", "desc1");
        toDelete.id = 4;
        eventRepository.events.add(toDelete);
        ResponseEntity<Event> response = sut.removeEvent(4);
        assertEquals(toDelete,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("deleteById", eventRepository.methods.getLast());
        //cleanup
        eventRepository.events.remove(toDelete);
    }

    @Test
    void testUpdateEvent(){
        Event toUpdate = new Event("test", new Date(10, 10, 2005), "owner1", "desc1");
        toUpdate.id = 4;
        eventRepository.events.add(toUpdate);
        Event expected = new Event("newName", new Date(10, 10, 2005), "owner1", "desc1");
        expected.id = 4;
        ResponseEntity<Event> response = sut.updateNameEvent(4, "newName");
        assertEquals(expected,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("save",eventRepository.methods.getLast());
        //cleanup
        eventRepository.events.removeLast();
    }


}
