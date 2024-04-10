package server.service;

import commons.Event;
import commons.Tag;
import commons.TagId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;

import java.util.Optional;

@Service
public class StatisticsService {
    private final ExpenseRepository exRep;
    private final EventRepository eventRep;
    private final TagRepository tagRep;
    @Autowired
    public StatisticsService(ExpenseRepository expenseRepository,
                             EventRepository eventRepository,
                             TagRepository tagRepository){
        exRep = expenseRepository;
        eventRep = eventRepository;
        this.tagRep = tagRepository;
    }

    public Double getSumByTag(int eventID, String  tagName){
        Optional<Event> optionalEvent = eventRep.findById(eventID);
        if(optionalEvent.isEmpty()){
            return null;
        }
        Tag t = tagRep.findTagByTagId(new TagId(tagName, optionalEvent.get()));
        if (t == null) {
            return null;
        }
        Double d =  exRep.findTotalAmountByTag(eventID, tagName);
        if (d == null) {
            d = 0.0;
        }
        return d;
    }

    public Double getTotalCost(int eventID){
        return (exRep.getTotalCostByEvent(eventID));
    }
}
