package server;

import commons.Event;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
//import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagementOverviewTest {
    @Test
    void getEventsTest() {
        ManagementOverview mnOverview = new ManagementOverview("events", "servers");
        assertEquals(new ArrayList<Event>(), mnOverview.getEvents());
    }

    @Test
    void getServersTest() {
        ManagementOverview mnOverview = new ManagementOverview("events", "servers");
        assertEquals(new ArrayList<String>(), mnOverview.getServerInstances());
    }
//    @Test
//    void deleteEventTest(){
//        ManagementOverview mnOverview = new ManagementOverview("events", "servers");
//        Event event = new Event("name", "date", new ArrayList<>(), "owner", new ArrayList<>(), "description");
//        //add event
//        assertEquals(event, mnOverview.deleteEvent(event));
//    }

//    @Test
//    void getJSONTest(){
//        ManagementOverview mnOverview = new ManagementOverview("events", "servers");
//        Event event = new Event("name", "date", new ArrayList<>(), "owner", new ArrayList<>(), "description");
//
//        assertEquals("JSON dump", mnOverview.downloadJSON(event));
//    }

    @Test
    void orderByTitle(){
        ManagementOverview mnOverview = new ManagementOverview("events", "servers");
        //Event event = new Event("name", "date", new ArrayList<>(), "owner", new ArrayList<>(), "description");
        assertEquals(new ArrayList<>(), mnOverview.orderByTitle());
    }
    @Test
    void oderByDate(){
        ManagementOverview mnOverview = new ManagementOverview("events", "servers");
        //Event event = new Event("name", "date", new ArrayList<>(), "owner", new ArrayList<>(), "description");
        assertEquals(new ArrayList<>(), mnOverview.orderByDate());
    }


    @Test
    void orderByLastActivity(){
        ManagementOverview mnOverview = new ManagementOverview("events", "servers");
        //Event event = new Event("name", "date", new ArrayList<>(), "owner", new ArrayList<>(), "description");
        assertEquals(new ArrayList<>(), mnOverview.orderByLastActivity());
    }
}