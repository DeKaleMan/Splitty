package server.api;

import commons.*;
import commons.dto.DebtDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DebtControllerTest {
    TestDebtRepository debtRepository = new TestDebtRepository();

    TestParticipantRepository participantRepository = new TestParticipantRepository();

    TestExpenseRepository expenseRepository = new TestExpenseRepository();

    TestEventRepository eventRepository = new TestEventRepository();

    DebtController sut;

    Event e1 = new Event("test1", new Date(10), "test1","test1");
    Event e2 = new Event("test2", new Date(20), "test2","test2");

    Participant p1 = new Participant("test1", 10.0, "test1", "test1", "test1", "","test1", e1);

    Participant p2 = new Participant("test2", 11.0, "test2", "test2", "test2", "","test2", e2);

    Expense expense1 = new Expense(e1, "test1", Type.Food, new Date(10), 10.0,p1);

    Expense expense2 = new Expense(e2, "test1", Type.Food, new Date(10), 1.0,p2);

    Expense expense3 = new Expense(e1, "test1", Type.Food, new Date(10), 10.0,p1);

    Debt d1 = new Debt(expense1,10.0,p1);

    Debt d2 = new Debt(expense2,1.0,p2);

    Debt d3 = new Debt(expense3,12.0,p1);

    @BeforeEach
    void setup() {
        sut = new DebtController(debtRepository, expenseRepository, participantRepository,
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
        List<Debt> expected = List.of(d1,d3);
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfEvent(1);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(expected,response.getBody());
        assertEquals("findByEvent",debtRepository.methods.getLast());
    }

    @Test
    void getAllDebtsOfEventInvalid() {
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfEvent(3);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void getAllDebtsOfExpense() {
        List<Debt> expected = List.of(d1);
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfExpense(1,1);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(expected,response.getBody());
        assertEquals("findByExpense",debtRepository.methods.getLast());
    }

    @Test
    void getAllDebtsOfExpenseInvalidExpense() {
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfExpense(1,4);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void getAllDebtsOfExpenseInvalidEvent() {
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfExpense(4,1);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void getAllDebtsOfParticipant() {
        List<Debt> expected = List.of(d1,d3);
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfParticipant(1,"test1");
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(expected,response.getBody());
        assertEquals("findByParticipant",debtRepository.methods.getLast());
    }

    @Test
    void getAllDebtsOfParticipantInvalidParticipant() {
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfParticipant(1,"test3");
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void getAllDebtsOfParticipantInvalidEvent() {
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfParticipant(4,"test1");
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void getALlDebts() {
        List<Debt> expected = List.of(d1,d2,d3);
        assertEquals(expected,sut.getALlDebts());
        assertEquals("findAll",debtRepository.methods.getLast());
    }

    @Test
    void saveDebt() {
        Debt expected = new Debt(expense1,25.0,p1);
        ResponseEntity<Debt> response = sut.saveDebt(new DebtDTO(25.0,1,1,"test1"));
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(expected,response.getBody());
        assertEquals("save",debtRepository.methods.getLast());
    }

    @Test
    void saveDebtInvalidExpense() {
        ResponseEntity<Debt> response = sut.saveDebt(new DebtDTO(25.0,1,4,"test1"));
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void saveDebtInvalidParticipant() {
        ResponseEntity<Debt> response = sut.saveDebt(new DebtDTO(25.0,1,1,"test3"));
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void saveDebtInvalidEvent() {
        ResponseEntity<Debt> response = sut.saveDebt(new DebtDTO(25.0,3,1,"test1"));
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
}