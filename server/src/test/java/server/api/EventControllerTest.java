package server.api;

import commons.Event;
import org.junit.jupiter.api.BeforeEach;

public class EventControllerTest {

    EventController sut;
    TestEventRepository eventRepository;
    Event event1 = new Event("test", "date", "owner", "desc");
    Event event2 = new Event("test1", "date1", "owner1", "desc1");

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        sut = new EventController(eventRepository);
        event1.id = 1;
        event2.id = 2;
        eventRepository.events.add(event1);
        eventRepository.events.add(event2);
    }
}
