package server;

import org.junit.jupiter.api.Test;

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
    @Test
    void createEventTest() {
        //change when events are live
        UserOverview userOverview = new UserOverview("name", "email");
        String event = "event";
        assertEquals(event, userOverview.createEvent("info"));
    }
    @Test
    void changeTitleTest() {
        //change when events are live
        UserOverview userOverview = new UserOverview("name", "email");
        String event = "event";
        userOverview.createEvent(event);
        assertFalse(userOverview.changeTitleEvent("event", null));
        assertFalse(userOverview.changeTitleEvent(null, "new"));
        assertFalse(userOverview.changeTitleEvent("notEvent", "new"));
        assertTrue(userOverview.changeTitleEvent(event, "newTitle"));

    }
}