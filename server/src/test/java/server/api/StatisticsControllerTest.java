package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.StatisticsService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class StatisticsControllerTest {
    @Mock
    StatisticsService service;
    @InjectMocks
    StatisticsController sut;

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
        MockitoAnnotations.openMocks(this);
        event1.id = 1;
        event2.id = 2;
        expense1.expenseId = 1;
        expense2.expenseId = 2;
        expense3.expenseId = 3;
        expense4.expenseId = 4;
    }

    @Test
    public void getTotalCostTest(){
        when(service.getTotalCost(anyInt())).thenReturn(expense1.getTotalExpense() + expense2.getTotalExpense());
        ResponseEntity<Double> res = sut.getTotalCost(event1.getId());
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(expense1.getTotalExpense() + expense2.getTotalExpense(), res.getBody());
    }

    @Test
    public void getPaymentsOfEvents(){
        //order = food, drinks, travel, other
        double[] expected = {0, 69.69, 0, 15.69};
        when(service.getPaymentsOfEvent(anyInt())).thenReturn(expected);
        ResponseEntity<double[]> res = sut.getPaymentsOfEvent(event1.id);
        assertArrayEquals(expected,res.getBody());
    }
    @Test
    public void invalidEventStats(){
        when(service.getPaymentsOfEvent(anyInt())).thenReturn(null);
        ResponseEntity<double[]> res = sut.getPaymentsOfEvent(1000024);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

}
