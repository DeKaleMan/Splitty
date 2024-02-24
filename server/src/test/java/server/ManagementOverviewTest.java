package server;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagementOverviewTest {
    @Test
    void getEventsTest() {
        ManagementOverview mnOverview = new ManagementOverview("events", "servers");
        assertEquals(new ArrayList<String>(), mnOverview.getEvents());
    }

    @Test
    void getServersTest() {
        ManagementOverview mnOverview = new ManagementOverview("events", "servers");
        assertEquals(new ArrayList<String>(), mnOverview.getServerInstances());
    }
    @Test
    void deleteEventTest(){
        ManagementOverview mnOverview = new ManagementOverview("events", "servers");
        String event = "";
        //add event
        assertEquals(event, mnOverview.deleteEvent(event));
    }

    @Test
    void getJSONTest(){
        ManagementOverview mnOverview = new ManagementOverview("events", "servers");
        String event = "";
        //add event
        assertEquals("JSON dump", mnOverview.downloadJSON(event));
    }



//    //orderings:
//    public List<String> orderByTitle(){
//        // return a list order by title using a query on the database
//        return new ArrayList<>();
//    }
//    public List<String> orderByDate(){
//        // return a list order by title using a query on the database
//        return new ArrayList<>();
//    }
//    public List<String> orderByLastActivity(){
//        // return a list order by title using a query on the database
//        return new ArrayList<>();
//    }

}