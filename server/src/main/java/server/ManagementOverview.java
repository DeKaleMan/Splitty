package server;

import java.util.ArrayList;
import java.util.List;

public class ManagementOverview {

    private String eventRepo;
    private String serverRepo;

    private ManagementOverview(String eventRepo, String serverRepo){
        // link to db later
        this.eventRepo = eventRepo;
        this.serverRepo = serverRepo;
    }


    private List<String> getEvents(){
        return new ArrayList<>();
    }

    private List<String> getServerInstances() {
        return new ArrayList<>();
    }

    private String deleteEvent(String event){
        //eventrepo query to delete the event;
        return event;
    }


    private String downloadJSON(String event){
        // query to get a JSON dump
        return "JSON dump";
    }

}
