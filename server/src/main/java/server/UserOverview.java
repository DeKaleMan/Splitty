package server;

import java.util.ArrayList;
import java.util.List;

public class UserOverview {

    //private final EventRepository repo;
    private String name;
    private String email;
    private List<String> events;
    public UserOverview(String name, String email){
        this.name = name;
        this.email = email;
        this.events = new ArrayList<>(); //add all events here
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getEvents() {
        return events;
    }

    public String createEvent(String info){
        //construct new event
        String event = "event";
        events.add(event);
        return event;
    }

    public boolean changeTitleEvent(String event, String title){
        if(event == null || title == null) return false;
        if(!events.contains(event)) return false;

        // return event.setTitle(title);
        return true;
    }
    public List<String> getExpenses(String event){
        List<String> res = new ArrayList<>(); //event.getExpenses();
        return res;
    }
}
