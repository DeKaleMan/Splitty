package service;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestEventRepository;
import server.api.TestExpenseRepository;
import server.service.StatisticsService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsServiceTest {
    StatisticsService sut;
    private TestExpenseRepository expenseRepository;
    private TestEventRepository eventRepository;
    Event event1 = new Event("test", new Date(10, 10, 2005), "owner", "desc");
    Event event2 = new Event("test1", new Date(10, 10, 2005), "owner1", "desc1");
    Event event3 = new Event("test2", new Date(10, 10, 2005), "owner1", "desc1");
    Participant p1 = new Participant("test", 10.0, "IBAN", "BIC", "email", "", "uuid", event1);

    Participant p2 = new Participant("test", 10.0, "IBAN", "BIC", "email1", "", "uuid2", event1);

    Participant p3 = new Participant("test", 10.0, "IBAN", "BIC", "email3", "", "uuid3", event2);

    Expense expense1 = new Expense(event1, "description", Type.Other, new Date(), 15.69, p1, true);
    Expense expense2 = new Expense(event1, "description", Type.Drinks, new Date(), 69.69, p2, true);
    Expense expense3 = new Expense(event2, "description", Type.Food, new Date(), 25.0, p3, true);
    Expense expense4 = new Expense(event2, "description", Type.Travel, new Date(), 50.0, p3, true);

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        expenseRepository = new TestExpenseRepository();
        sut = new StatisticsService(expenseRepository, eventRepository);
        event1.id = 1;
        event2.id = 2;
        expense1.expenseId = 1;
        expense2.expenseId = 2;
        expense3.expenseId = 3;
        expense4.expenseId = 4;
        expenseRepository.expenses.add(expense1);
        expenseRepository.expenses.add(expense2);
        expenseRepository.expenses.add(expense3);
        expenseRepository.expenses.add(expense4);
        eventRepository.events.add(event1);
        eventRepository.events.add(event2);
    }

    @Test
    public void getTotalCostTest() {
        Double actual = sut.getTotalCost(event1.getId());
        assertEquals(expense1.getTotalExpense() + expense2.getTotalExpense(), actual);
    }

    @Test
    public void getPaymentsOfEvents() {
        double[] actual1 = sut.getPaymentsOfEvent(event1.id);
        //order = food, drinks, travel, other
        double[] expected1 = {0, 69.69, 0, 15.69};
        assertArrayEquals(expected1, actual1);

        double[] actual2 = sut.getPaymentsOfEvent(event2.id);
        //order = food, drinks, travel, other
        double[] expected2 = {25.0, 0, 50.0, 0};
        assertArrayEquals(expected2, actual2);
    }

    @Test
    public void nullEventStats() {
        double[] actual = sut.getPaymentsOfEvent(1000024);
        assertNull(actual);
    }

    @Test
    public void emptyEventSum() {
        Double actual = sut.getTotalCost(event3.getId());
        assertEquals(0, actual);
    }

}