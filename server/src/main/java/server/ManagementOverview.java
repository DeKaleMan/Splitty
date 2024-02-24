package server;

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


    public List<String> getEvents(){
        return new ArrayList<>();
    }

    public List<String> getServerInstances() {
        return new ArrayList<>();
    }

    public String deleteEvent(String event){
        //eventrepo query to delete the event;

        return event;
    }


    public String downloadJSON(String event){
        // query to get a JSON dump
        return "JSON dump";
    }

    //orderings:
    public List<String> orderByTitle(){
        // return a list order by title using a query on the database
        return new ArrayList<>();
    }
    public List<String> orderByDate(){
        // return a list order by title using a query on the database
        return new ArrayList<>();
    }
    public List<String> orderByLastActivity(){
        // return a list order by title using a query on the database
        return new ArrayList<>();
    }

}
