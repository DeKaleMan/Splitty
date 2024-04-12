package service;

import commons.Event;
import commons.dto.EventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestEventRepository;
import server.service.EventService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EventServiceTest {

    EventService sut;
    TestEventRepository eventRepository;
    Event event1 = new Event("test1", new Date(10, 10, 2005), "owner1", "desc1");
    Event event2 = new Event("test2", new Date(10, 10, 2005), "owner2", "desc2");
    Event event3 = new Event("test3", new Date(10, 10, 2005), "owner3", "desc3");

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        sut = new EventService(eventRepository);
        event1.id = 1;
        event2.id = 2;
        event3.id = 3;
        eventRepository.events.addAll(List.of(event1, event2, event3));
    }

    // testing whether it works if one entry is null
    @Test
    void nullSave() {
        assertNull(sut.saveEvent(null));
    }

    @Test
    void nullSaveName() {
        EventDTO e = new EventDTO(null, new Date(10, 10, 2005), "owner1", "desc1");
        Event actual = sut.saveEvent(e);
        assertNull(actual);
    }

    @Test
    void nullSaveDate() {
        EventDTO e = new EventDTO("test", null, "owner1", "desc1");
        Event actual = sut.saveEvent(e);
        assertNull(actual);
    }

    @Test
    void nullSaveDescription() {
        EventDTO e = new EventDTO("test", new Date(10, 10, 2005), "owner1", null);
        Event actual = sut.saveEvent(e);
        assertNull(actual);
    }

    @Test
    void nullSaveOwner() {
        EventDTO e = new EventDTO("test", new Date(10, 10, 2005), null, "desc1");
        Event actual = sut.saveEvent(e);
        assertNull(actual);
    }

    @Test
    void emptyOwner() {
        EventDTO e = new EventDTO("test", new Date(10, 10, 2005), "", "desc1");
        Event actual = sut.saveEvent(e);
        assertNull(actual);
    }

    @Test
    void emptyName() {
        EventDTO e = new EventDTO("", new Date(10, 10, 2005), "owner1", "desc1");
        Event actual = sut.saveEvent(e);
        assertNull(actual);
    }

    @Test
    void testSave() {
        EventDTO e = new EventDTO("test", new Date(10, 10, 2005), "owner1", "desc1");
        Event actual = sut.saveEvent(e);
        assertEquals("save", eventRepository.methods.getLast());
        Event expected = new Event("test", new Date(10, 10, 2005), "owner1", "desc1");
        assertEquals(expected, actual);
        //cleanup
        eventRepository.events.removeLast();
    }

    @Test
    void testFindById() {
        Event actual = sut.getEventById(1);
        assertEquals(event1, actual);
    }

    @Test
    void testFindByIdInvalid() {
        Event actual = sut.getEventById(-1);
        assertNull(actual);
    }

    @Test
    void testFindAll() {
        List<Event> expected = List.of(event1, event2, event3);
        List<Event> actual = sut.getAllEvents();
        assertEquals(expected, actual);
        assertEquals("findAll", eventRepository.methods.getLast());
    }

    @Test
    void testDeleteEvent() {
        Event toDelete = new Event("test", new Date(10, 10, 2005), "owner1", "desc1");
        toDelete.id = 4;
        eventRepository.events.add(toDelete);
        Event actual = sut.removeEvent(4);
        assertEquals(toDelete, actual);
        assertEquals("deleteById", eventRepository.methods.getLast());
        //cleanup
        eventRepository.events.remove(toDelete);
    }

    @Test
    void testDeleteEventInvalid() {
        Event actual = sut.removeEvent(-1);
        assertNull(actual);
    }

    @Test
    void testUpdateEvent() {
        Event toUpdate = new Event("test", new Date(10, 10, 2005), "owner1", "desc1");
        toUpdate.id = 4;
        eventRepository.events.add(toUpdate);
        Event expected = new Event("newName", new Date(10, 10, 2005), "owner1", "desc1");
        expected.id = 4;
        Event actual = sut.updateEventName(4, "newName");
        assertEquals(expected, actual);
        assertEquals("save", eventRepository.methods.getLast());
        //cleanup
        eventRepository.events.removeLast();
    }

    @Test
    void testUpdateEventInvalid() {
        Event actual = sut.updateEventName(-1, "newName");
        assertNull(actual);
    }
}