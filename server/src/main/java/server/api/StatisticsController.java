package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.StatisticsService;



@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private StatisticsService statisticsService;
    @Autowired
    public StatisticsController(StatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    @GetMapping("/{eventId}/{tagName}")
    public ResponseEntity<Double> getPaymentsOfEvent(@PathVariable("eventId") int eventID,
                                                     @PathVariable("tagName") String tagName){
        Double res = statisticsService.getSumByTag(eventID, tagName);
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
