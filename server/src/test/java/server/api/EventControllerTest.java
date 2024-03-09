package server.api;
import commons.EventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.*;

public class EventControllerTest {

    EventController ctrl;
    TestEventRepository eventRepository;
    EventDTO event1 = new EventDTO("test", "date", "owner", "desc");
    EventDTO event2 = new EventDTO("test1", "date1", "owner1", "desc1");

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        ctrl = new EventController(eventRepository);
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
    void EmptyOwner() {
        event1.setOwner("");
        assertEquals(BAD_REQUEST, ctrl.saveEvent(event1).getStatusCode());
    }
    @Test
    void EmptyDate() {
        event1.setDate("");
        assertEquals(BAD_REQUEST, ctrl.saveEvent(event1).getStatusCode());
    }
    @Test
    void EmptyName() {
        event1.setName("");
        assertEquals(BAD_REQUEST, ctrl.saveEvent(event1).getStatusCode());
    }
}
