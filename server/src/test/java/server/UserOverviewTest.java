package server;

import commons.Event;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserOverviewTest {

    @Test
    void getName() {
        UserOverview userOverview = new UserOverview("name", "email");
        assertEquals("name", userOverview.getName());
    }

    @Test
    void getEmail() {
        UserOverview userOverview = new UserOverview("name", "email");
        assertEquals("email", userOverview.getEmail());
    }
//    @Test
//    void createEventTest() {
//        UserOverview userOverview = new UserOverview("name", "email");
//        Event event = new Event("name", "date",
//                new ArrayList<>(), "owner", new ArrayList<>(), "description");
//        assertEquals(event, userOverview.createEvent("name", "date",
//                new ArrayList<>(), "owner", new ArrayList<>(), "description"));
//    }
//    @Test
//    void getEventTest(){
//        UserOverview userOverview = new UserOverview("name", "email");
//        userOverview.createEvent("name", "date",
//                new ArrayList<>(), "owner", new ArrayList<>(), "description");
//        List<Event> list = new ArrayList<>();
//        list.add(new Event("name", "date",
//                new ArrayList<>(), "owner", new ArrayList<>(), "description"));
//        assertEquals(list, userOverview.getEvents());
//    }

//    @Test
//    void changeTitleTest() {
//        //change when events are live
//        UserOverview userOverview = new UserOverview("name", "email");
//        Event event = new Event("name", "date", new ArrayList<>(), "owner", new ArrayList<>(), "description");
//        userOverview.createEvent("name", "date", new ArrayList<>(), "owner", new ArrayList<>(), "description");
//        assertFalse(userOverview.changeTitleEvent(event, null));
//        assertFalse(userOverview.changeTitleEvent(null, "new"));
//        assertFalse(userOverview.changeTitleEvent(new Event("", "",
//                new ArrayList<>(),"",  new ArrayList<>(), ""), "new"));
//        assertTrue(userOverview.changeTitleEvent(event, "newTitle"));
//
//    }
}