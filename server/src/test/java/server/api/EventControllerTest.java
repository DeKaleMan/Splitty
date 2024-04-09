package server.api;
import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.EventService;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class EventControllerTest {

    @Mock
    EventService service;
    @InjectMocks
    EventController sut;
    Event event1 = new Event("test1", new Date(10, 10, 2005), "owner1", "desc1");
    Event event2 = new Event("test2", new Date(10, 10, 2005), "owner2", "desc2");
    Event event3 = new Event("test3", new Date(10, 10, 2005), "owner3", "desc3");

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        event1.id = 1;
        event2.id = 2;
        event3.id = 3;

    }

    // testing whether it works if one entry is null
    @Test
    void testSaveInvalid() {
        when(service.saveEvent(any(EventDTO.class))).thenReturn(null);
        assertEquals(BAD_REQUEST, sut.saveEvent(null).getStatusCode());
    }

    @Test
    void testSave() {
        EventDTO e = new EventDTO("test", new Date(10, 10, 2005), "owner1", "desc1");
        Event expected = new Event("test", new Date(10, 10, 2005), "owner1", "desc1");
        when(service.saveEvent(any(EventDTO.class))).thenReturn(expected);
        ResponseEntity<Event> response = sut.saveEvent(e);
        assertEquals(expected, response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void testFindById() {
        when(service.getEventById(anyInt())).thenReturn(event1);
        ResponseEntity<Event> response = sut.getEventById(1);
        assertEquals(event1,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void testFindByIdInvalid() {
        when(service.getEventById(anyInt())).thenReturn(null);
        ResponseEntity<Event> response = sut.getEventById(-1);
        assertEquals(BAD_REQUEST,response.getStatusCode());
    }

    @Test
    void testFindAll() {
        List<Event> expected = List.of(event1, event2, event3);
        when(service.getAllEvents()).thenReturn(expected);
        ResponseEntity<List<Event>> response = sut.getAllEvents();
        assertEquals(expected,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void testDeleteEvent(){
        when(service.removeEvent(anyInt())).thenReturn(event3);

        ResponseEntity<Event> response = sut.removeEvent(3);
        assertEquals(event3,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());

    }

    @Test
    void testDeleteEventInvalid(){
        when(service.removeEvent(anyInt())).thenReturn(null);
        ResponseEntity<Event> response = sut.removeEvent(-1);
        assertEquals(NOT_FOUND,response.getStatusCode());
    }

    @Test
    void testUpdateEvent(){
        Event expected = new Event("newName", new Date(10, 10, 2005), "owner1", "desc1");
        when(service.updateEventName(anyInt(), anyString())).thenReturn(expected);
        ResponseEntity<Event> response = sut.updateNameEvent(4, "newName");
        assertEquals(expected,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());

    }

    @Test
    void testUpdateEventInvalid(){
        when(service.updateEventName(anyInt(),anyString())).thenReturn(null);
        ResponseEntity<Event> response = sut.updateNameEvent(-1, "newName");
        assertEquals(NOT_FOUND,response.getStatusCode());
    }


}
