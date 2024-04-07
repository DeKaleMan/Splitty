package server.api;

import commons.Event;
import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.service.StatisticsService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private StatisticsService statisticsService;
    @Autowired
    public StatisticsController(StatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    @GetMapping(path = {"/", ""})
    public ResponseEntity<double[]> getPaymentsOfEvent(@RequestParam("eventID") int eventID){
        double[] res = statisticsService.getPaymentsOfEvent(eventID);
        return (res == null) ?
            ResponseEntity.notFound().build() :
            ResponseEntity.ok(res);
    }

    @GetMapping("/totalCost")
    public ResponseEntity<Double> getTotalCost(@RequestParam("eventID") int eventID){
        double res = statisticsService.getTotalCost(eventID);
        return ResponseEntity.ok(res);
    }

}
