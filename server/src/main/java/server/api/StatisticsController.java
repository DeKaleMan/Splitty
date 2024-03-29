package server.api;

import commons.Event;
import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ExpenseRepository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private ExpenseRepository exRep;
    private EventRepository eventRep;
    @Autowired
    public StatisticsController(ExpenseRepository expenseRepository,
                                EventRepository eventRepository){
        exRep = expenseRepository;
        eventRep = eventRepository;
    }

    @GetMapping(path = {"/", ""})
    public ResponseEntity<double[]> getPaymentsOfEvent(@RequestParam("eventID") int eventID){
        Optional<Event> optionalEvent = eventRep.findById(eventID);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<Expense> expenses = exRep.findByEvent(optionalEvent.get());
        double[] stat = new double[4];
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

        return ResponseEntity.ok(stat);
    }

    @GetMapping("/totalCost")
    public ResponseEntity<Double> getTotalCost(@RequestParam("eventID") int eventID){
        double totalCost = (exRep.getTotalCostByEvent(eventID));

        return ResponseEntity.ok(totalCost);
    }

}
