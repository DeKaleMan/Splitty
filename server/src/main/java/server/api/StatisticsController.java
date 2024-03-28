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
    public ResponseEntity<int[]> getPaymentsOfEvent(@RequestParam("eventId") int eventId){
        Optional<Event> optionalEvent = eventRep.findById(eventId);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<Expense> expenses = exRep.findByEvent(optionalEvent.get());
        int[] stat = new int[4];
        for (Expense expense : expenses) {
            switch (expense.getType()) {
                case Food:
                    stat[0]++;
                    break;
                case Drinks:
                    stat[1]++;
                    break;
                case Travel:
                    stat[2]++;
                    break;
                case Other:
                    stat[3]++;
                    break;
                default:
                    // Handle unexpected expense types here, if needed
                    break;
            }
        }

        return ResponseEntity.ok(stat);
    }

}
