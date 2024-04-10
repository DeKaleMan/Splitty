package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
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
import static org.mockito.ArgumentMatchers.anyString;
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
    Tag t1 = new Tag(event1, "Food", "#2a8000");
    Tag t2 = new Tag(event1, "Travel", "#3700ff");
    Tag t3 = new Tag(event2, "Entrance Fees", "#c50000");

    Expense expense1 = new Expense(event1, "description", t1, new Date(), 15.69, p1, true);
    Expense expense2 =  new Expense(event1, "description",t1, new Date(), 69.69, p2, true);
    Expense expense3 =  new Expense(event2, "description", t2, new Date(), 25.0, p3, true);
    Expense expense4 =  new Expense(event2, "description", t2, new Date(), 50.0, p3, true);

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
    public void getSumByTagTest(){
        Double expected = 15.69;
        when(service.getSumByTag(anyInt(), anyString())).thenReturn(expected);
        ResponseEntity<Double> res = sut.getPaymentsOfEvent(event1.id, "Food");
        assertEquals(expected,res.getBody());
    }
    @Test
    public void invalidEventStats(){
        when(service.getSumByTag(anyInt(), anyString())).thenReturn(null);
        ResponseEntity<Double> res = sut.getPaymentsOfEvent(1000024, "2324225252");
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

}
