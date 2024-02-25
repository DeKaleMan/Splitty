package server;

import commons.Event;

import java.util.ArrayList;
import java.util.List;

public class ManagementOverview {

    private String eventRepo;
    private String serverRepo;

    public ManagementOverview(String eventRepo, String serverRepo){
        // link to db later
        this.eventRepo = eventRepo;
        this.serverRepo = serverRepo;
    }


    public List<Event> getEvents(){
        return new ArrayList<>();
    }

    public List<String> getServerInstances() {
        return new ArrayList<>();
    }

    public Event deleteEvent(Event event){
        //eventrepo query to delete the event;

        return event;
    }


    public String downloadJSON(Event event){
        // query to get a JSON dump
        return "JSON dump";
    }

    //orderings:
    public List<Event> orderByTitle(){
        // return a list order by title using a query on the database
        return new ArrayList<>();
    }
    public List<Event> orderByDate(){
        // return a list order by title using a query on the database
        return new ArrayList<>();
    }
    public List<Event> orderByLastActivity(){
        // return a list order by title using a query on the database
        return new ArrayList<>();
    }

}
