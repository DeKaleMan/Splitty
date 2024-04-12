package service;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestEventRepository;
import server.api.TestExpenseRepository;
import server.api.TestTagRepository;
import server.service.StatisticsService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsServiceTest {
    StatisticsService sut;
    private TestExpenseRepository expenseRepository;
    private TestEventRepository eventRepository;
    private TestTagRepository tagRepository;
    Event event1 = new Event("test", new Date(10, 10, 2005), "owner", "desc");
    Event event2 = new Event("test1", new Date(10, 10, 2005), "owner1", "desc1");
    Event event3 = new Event("test2", new Date(10, 10, 2005), "owner1", "desc1");
    Participant p1 = new Participant("test", 10.0, "IBAN", "BIC", "email", "", "uuid", event1);

    Participant p2 = new Participant("test", 10.0, "IBAN", "BIC", "email1", "", "uuid2", event1);

    Participant p3 = new Participant("test", 10.0, "IBAN", "BIC", "email3", "", "uuid3", event2);

    Tag t1 = new Tag(event1, "Food", "#2a8000");
    Tag t2 = new Tag(event1, "Entrance Fees", "#c50000");
    Tag t12 = new Tag(event2, "Food", "#2a8000");
    Tag t22 = new Tag(event2, "Entrance Fees", "#c50000");

    Expense expense1 = new Expense(event1, "description", t1, new Date(), 15.69, p1, true);
    Expense expense2 = new Expense(event1, "description", t2, new Date(), 69.69, p2, true);
    Expense expense3 = new Expense(event2, "description", t22, new Date(), 25.0, p3, true);
    Expense expense4 = new Expense(event2, "description", t22, new Date(), 50.0, p3, true);

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        expenseRepository = new TestExpenseRepository();
        tagRepository = new TestTagRepository();
        sut = new StatisticsService(expenseRepository, eventRepository, tagRepository);
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
        tagRepository.tags.add(t1);
        tagRepository.tags.add(t2);
        tagRepository.tags.add(t12);
        tagRepository.tags.add(t22);
    }

    @Test
    public void getTotalCostTest() {
        Double actual = sut.getTotalCost(event1.getId());
        assertEquals(expense1.getTotalExpense() + expense2.getTotalExpense(), actual);
    }

    @Test
    public void getSumByTagTest() {
        Double actual = sut.getSumByTag(event1.id, t1.getName());
        assertEquals(15.69, actual);
        Double actual2 = sut.getSumByTag(event2.id, t22.getName());
        assertEquals(75.0, actual2);
    }
    @Test
    public void getSumByTagInvalidNameTest() {
        Double actual = sut.getSumByTag(event1.id, "");
        assertEquals(null, actual);

    }

    @Test
    public void nullEventStats() {
        Double actual = sut.getSumByTag(1000024, "WEAEAWDAWDAWEARAWRAWRC");
        assertNull(actual);
    }

    @Test
    public void emptyEventSum() {
        Double actual = sut.getTotalCost(event3.getId());
        assertEquals(0, actual);
    }

}