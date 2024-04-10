package server.service;

import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;

import java.util.Optional;

@Service
public class StatisticsService {
    private ExpenseRepository exRep;
    private EventRepository eventRep;
    @Autowired
    public StatisticsService(ExpenseRepository expenseRepository,
                                EventRepository eventRepository){
        exRep = expenseRepository;
        eventRep = eventRepository;
    }

    public Double getPaymentsOfEvent(int eventID, String  tagName){
        Optional<Event> optionalEvent = eventRep.findById(eventID);
        if(optionalEvent.isEmpty()){
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
