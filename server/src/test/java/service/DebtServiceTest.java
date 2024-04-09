package service;

import commons.*;
import commons.dto.DebtDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.*;
import server.service.DebtService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DebtServiceTest {
    TestDebtRepository debtRepository = new TestDebtRepository();

    TestParticipantRepository participantRepository = new TestParticipantRepository();

    TestExpenseRepository expenseRepository = new TestExpenseRepository();

    TestEventRepository eventRepository = new TestEventRepository();

    DebtService sut;

    Event e1 = new Event("test1", new Date(10), "test1", "test1");
    Event e2 = new Event("test2", new Date(20), "test2", "test2");

    Participant p1 = new Participant("test1", 10.0, "test1", "test1", "test1", "", "test1", e1);

    Participant p2 = new Participant("test2", 11.0, "test2", "test2", "test2", "", "test2", e2);

    Expense expense1 = new Expense(e1, "test1", Type.Food, new Date(10), 10.0, p1, true);

    Expense expense2 = new Expense(e2, "test1", Type.Food, new Date(10), 1.0, p2, true);

    Expense expense3 = new Expense(e1, "test1", Type.Food, new Date(10), 10.0, p1, true);

    Debt d1 = new Debt(expense1, 10.0, p1);

    Debt d2 = new Debt(expense2, 1.0, p2);

    Debt d3 = new Debt(expense3, 12.0, p1);

    @BeforeEach
    void setup() {

        sut = new DebtService(debtRepository, expenseRepository, participantRepository,
            eventRepository);
        e1.id = 1;
        e2.id = 2;
        eventRepository.events.add(e1);
        eventRepository.events.add(e2);

        participantRepository.participants.add(p1);
        participantRepository.participants.add(p2);

        expense1.expenseId = 1;
        expense2.expenseId = 2;
        expense3.expenseId = 3;
        expenseRepository.expenses.add(expense1);
        expenseRepository.expenses.add(expense2);
        expenseRepository.expenses.add(expense3);

        debtRepository.debts.add(d1);
        debtRepository.debts.add(d2);
        debtRepository.debts.add(d3);
    }


    @Test
    void getAllDebtsOfEvent() {
        List<Debt> expected = List.of(d1, d3);
        List<Debt> actual = sut.getAllDebtsOfEvent(1);
        assertEquals(expected, actual);
        assertEquals("findByEvent", debtRepository.methods.getLast());
    }

    @Test
    void getAllDebtsOfEventInvalid() {
        List<Debt> actual = sut.getAllDebtsOfEvent(3);
        assertNull(actual);
    }

    @Test
    void getAllDebtsOfExpense() {
        List<Debt> expected = List.of(d1);
        List<Debt> actual = sut.getAllDebtsOfExpense(1, 1);
        assertEquals(expected, actual);
        assertEquals("findByExpense", debtRepository.methods.getLast());
    }

    @Test
    void getAllDebtsOfExpenseInvalidExpense() {
        List<Debt> actual = sut.getAllDebtsOfExpense(1, 4);
        assertNull(actual);
    }

    @Test
    void getAllDebtsOfExpenseInvalidEvent() {
        List<Debt> actual = sut.getAllDebtsOfExpense(4, 1);
        assertNull(actual);
    }

    @Test
    void getAllDebtsOfParticipant() {
        List<Debt> expected = List.of(d1, d3);
        List<Debt> actual = sut.getAllDebtsOfParticipant(1, "test1");
        assertEquals(expected, actual);
        assertEquals("findByParticipant", debtRepository.methods.getLast());
    }

    @Test
    void getAllDebtsOfParticipantInvalidParticipant() {
        List<Debt> actual = sut.getAllDebtsOfParticipant(1, "test3");
        assertNull(actual);
    }

    @Test
    void getAllDebtsOfParticipantInvalidEvent() {
        List<Debt> actual = sut.getAllDebtsOfParticipant(4, "test1");
        assertNull(actual);
    }

    @Test
    void getALlDebts() {
        List<Debt> expected = List.of(d1, d2, d3);
        List<Debt> actual = sut.getALlDebts();
        assertEquals(expected, actual);
        assertEquals("findAll", debtRepository.methods.getLast());
    }

    @Test
    void saveDebt() {
        Debt expected = new Debt(expense1, 25.0, p1);
        Debt actual = sut.saveDebt(new DebtDTO(25.0, 1, 1, "test1"));
        assertEquals(expected, actual);
        assertEquals("save", debtRepository.methods.getLast());
        //cleanup
        debtRepository.debts.removeLast();
    }

    @Test
    void saveDebtInvalidExpense() {
        Debt actual = sut.saveDebt(new DebtDTO(25.0, 1, 4, "test1"));
        assertNull(actual);
    }

    @Test
    void saveDebtInvalidParticipant() {
        Debt actual = sut.saveDebt(new DebtDTO(25.0, 1, 1, "test3"));
        assertNull(actual);
    }

    @Test
    void saveDebtInvalidEvent() {
        Debt actual = sut.saveDebt(new DebtDTO(25.0, 3, 1, "test1"));
        assertNull(actual);
    }

    @Test
    void testDeleteDebts() {
        Expense expense4 = new Expense(e1, "test1", Type.Food, new Date(10), 10.0, p1, true);
        expenseRepository.expenses.add(expense4);
        expense4.expenseId = 4;
        List<Debt> expected = List.of(new Debt(expense4, 1, p1), new Debt(expense4, 2, p1));
        debtRepository.debts.addAll(expected);
        List<Debt> actual = sut.deleteDebtsOfExpense(1, 4);
        assertEquals(expected, actual);
        assertEquals("deleteDebtsByExpense", debtRepository.methods.getLast());
    }

    @Test
    void testDeleteDebtsInvalid() {
        List<Debt> actual = sut.deleteDebtsOfExpense(-1, 4);
        assertNull(actual);
    }

}