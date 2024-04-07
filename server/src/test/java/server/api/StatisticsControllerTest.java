package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.StatisticsService;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StatisticsControllerTest {
    StatisticsController sut;
    private TestExpenseRepository expenseRepository;
    private TestEventRepository eventRepository;
    Event event1 = new Event("test", new Date(10, 10, 2005), "owner", "desc");
    Event event2 = new Event("test1", new Date(10, 10, 2005), "owner1", "desc1");
    Event event3 = new Event("test2", new Date(10, 10, 2005), "owner1", "desc1");
    Participant p1 = new Participant("test", 10.0,"IBAN","BIC","email","","uuid",event1);

    Participant p2 = new Participant("test", 10.0,"IBAN","BIC","email1","","uuid2",event1);

    Participant p3 = new Participant("test", 10.0,"IBAN","BIC","email3","","uuid3",event2);

    Expense expense1 = new Expense(event1, "description", Type.Other, new Date(), 15.69, p1, true);
    Expense expense2 =  new Expense(event1, "description", Type.Drinks, new Date(), 69.69, p2, true);
    Expense expense3 =  new Expense(event2, "description", Type.Food, new Date(), 25.0, p3, true);
    Expense expense4 =  new Expense(event2, "description", Type.Travel, new Date(), 50.0, p3, true);

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        expenseRepository = new TestExpenseRepository();
        StatisticsService statisticsService = new StatisticsService(expenseRepository, eventRepository);
        sut = new StatisticsController(statisticsService);
        event1.id = 1;
        event2.id = 2;
        expenseRepository.save(expense1);
        expenseRepository.save(expense2);
        expenseRepository.save(expense3);
        expenseRepository.save(expense4);
        eventRepository.events.add(event1);
        eventRepository.events.add(event2);
    }

    @Test
    public void getTotalCostTest(){
        ResponseEntity<Double> res = sut.getTotalCost(event1.getId());
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(expense1.getTotalExpense() + expense2.getTotalExpense(), res.getBody());
    }

    @Test
    public void getPaymentsOfEvents(){
        ResponseEntity<double[]> res1 = sut.getPaymentsOfEvent(event1.id);
        //order = food, drinks, travel, other
        double[] expected1 = {0, 69.69, 0, 15.69};
        assertTrue(Arrays.equals(expected1,res1.getBody()));

        ResponseEntity<double[]> res2 = sut.getPaymentsOfEvent(event2.id);
        //order = food, drinks, travel, other
        double[] expected2 = {25.0, 0, 50.0, 0};
        assertTrue(Arrays.equals(expected2,res2.getBody()));
    }
    @Test
    public void nullEventStats(){
        ResponseEntity<double[]> res = sut.getPaymentsOfEvent(1000024);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void emptyEventSum(){
        ResponseEntity<Double> res = sut.getTotalCost(event3.getId());
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(0, res.getBody());
    }
}
