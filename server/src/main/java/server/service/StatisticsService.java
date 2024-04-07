package server.service;

import commons.Event;
import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;

import java.util.List;
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

    public double[] getPaymentsOfEvent(int eventID){
        Optional<Event> optionalEvent = eventRep.findById(eventID);
        if(optionalEvent.isEmpty()){
            return null;
        }
        List<Expense> expenses = exRep.findByEvent(optionalEvent.get());
        double[] stat = new double[4];
        //order = food, drinks, travel, other
        for (Expense expense : expenses) {
            switch (expense.getType()) {
                case Food:
                    stat[0]+=expense.getTotalExpense();
                    break;
                case Drinks:
                    stat[1]+=expense.getTotalExpense();
                    break;
                case Travel:
                    stat[2]+=expense.getTotalExpense();
                    break;
                case Other:
                    stat[3]+=expense.getTotalExpense();
                    break;
                default:
                    // Handle unexpected expense types here, if needed
                    break;
            }
        }

        return stat;
    }

    public Double getTotalCost(int eventID){

        return (exRep.getTotalCostByEvent(eventID));
    }
}
