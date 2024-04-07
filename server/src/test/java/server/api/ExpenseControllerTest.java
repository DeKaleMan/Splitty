package server.api;

import commons.*;
import commons.dto.ExpenseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.service.ExpenseService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

class ExpenseControllerTest {
    private ExpenseController sut;
    private TestExpenseRepository expenseRepository;
    private TestEventRepository eventRepository;
    private TestParticipantRepository participantRepository;

    Event event1 = new Event("test", new Date(10, 10, 2005), "owner", "desc");
    Event event2 = new Event("test1", new Date(10, 10, 2005), "owner1", "desc1");

    Participant p1 = new Participant("test", 10.0,"IBAN","BIC","email","","uuid",event1);

    Participant p2 = new Participant("test", 10.0,"IBAN","BIC","email1","","uuid2",event1);

    Participant p3 = new Participant("test", 10.0,"IBAN","BIC","email3","","uuid3",event2);

    Date date = new Date();
    Expense e1 = new Expense(event1, "", Type.Drinks,
        date, 0.0, p1,true);
    Expense e2 = new Expense(event1, "", Type.Drinks,
        date, 0.0, p2,true);
    Expense e3 = new Expense(event2, "", Type.Drinks,
        date, 0.0, p1,true);

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        expenseRepository = new TestExpenseRepository();
        participantRepository = new TestParticipantRepository();
        ExpenseService expenseService = new ExpenseService(expenseRepository,eventRepository,participantRepository);
        sut = new ExpenseController(expenseService);
        event1.id = 1;
        event2.id = 2;
        eventRepository.events.add(event1);
        eventRepository.events.add(event2);
        participantRepository.participants.add(p1);
        participantRepository.participants.add(p2);
        participantRepository.participants.add(p3);
        expenseRepository.expenses.add(e1);
        expenseRepository.expenses.add(e2);
        expenseRepository.expenses.add(e3);
        e1.expenseId = 1;
        e2.expenseId = 2;
        e3.expenseId = 3;
    }

    @Test
    void nullSave() {
        var actual = sut.saveExpense(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveDate() {
        var actual =
            sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks,
                null, 0.0, "uuid",true));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveUuid() {
        var actual = sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks,
            new Date(), 0.0, null,true));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveNegative() {
        var actual =
            sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks,
                new Date(), -1.0, "uuid",true));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void emptyUuid() {
        var actual = sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks,
            new Date(), 0.0, "",true));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void testSave() {
        Date date = new Date();
        var actual =
            sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks,
                date, 0.0, "uuid",true));
        assertEquals(new Expense(event1, "", Type.Drinks,
                date, 0.0, p1,true),
            actual.getBody());
        assertEquals("save", expenseRepository.methods.getLast());
        //cleanup
        expenseRepository.expenses.removeLast();
    }

    @Test
    void testSaveInvalidEvent() {
        ResponseEntity<Expense> response = sut.saveExpense(new ExpenseDTO(-1, "", Type.Drinks,
            date, 0.0, "uuid",true));
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetByEventCode() {
        List<Expense> expected = List.of(e1, e2);
        var actual = sut.findByEventCode(1).getBody();
        assertEquals(expected, actual);
        assertEquals("findByEvent", expenseRepository.methods.getLast());
    }

    @Test
    void testGetByEventCodeInvalid() {
        ResponseEntity<List<Expense>> response = sut.findByEventCode(-1);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetByEventCodeAndPayerUuid() {
        List<Expense> expected = List.of(e1);
        var actual = sut.findByEventCodeAndPayerUuid(1, "uuid").getBody();
        assertEquals(expected, actual);
        assertEquals("findByEventAndPayerUuid", expenseRepository.methods.getLast());
    }

    @Test
    void testGetByEventCodeAndPayerUuidInvalid() {
        ResponseEntity<List<Expense>> response = sut.findByEventCodeAndPayerUuid(-1,"invalid");
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdate(){
        Expense toUpdate = new Expense(event1,"d", Type.Drinks, new Date(), 1.0, p1,
            true);
        toUpdate.expenseId = 4;
        expenseRepository.expenses.add(toUpdate);
        Expense updated = new Expense(event1,"d2", Type.Food, new Date(1,1,1), 2.0,
            p2, false);
        updated.expenseId = 4;
        ResponseEntity<Expense> response = sut.updateExpense(1,4,new ExpenseDTO(1,"d2",
            Type.Food, new Date(1,1,1), 2.0, "uuid2", false));
        assertEquals(updated,response.getBody());
        assertEquals(OK,response.getStatusCode());
        assertEquals("save", expenseRepository.methods.getLast());
        //cleanup
        expenseRepository.expenses.removeIf(x -> x.expenseId == 4 && x.getEvent().equals(event1));
    }

    @Test
    void testUpdateInvalidEvent(){
        ResponseEntity<Expense> response = sut.updateExpense(-1,1, new ExpenseDTO());
        assertEquals(BAD_REQUEST,response.getStatusCode());
    }

    @Test
    void testUpdateInvalidExpense(){
        ResponseEntity<Expense> response = sut.updateExpense(1,-1, new ExpenseDTO());
        assertEquals(BAD_REQUEST,response.getStatusCode());
    }

    @Test
    void testIncorrectDelete1(){
        var actual = sut.deleteExpenseByEventIdAndExpenseId(0, 0);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        Date date = new Date();
        ExpenseDTO e1 = new ExpenseDTO(event1.id, "", Type.Drinks,
                date, 0.0, "uuid",true);
        var actual2 = sut.deleteExpenseByEventIdAndExpenseId(event1.id, -33);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void testIncorrectDelete2(){
        ResponseEntity<Expense> response = sut.deleteExpenseByEventIdAndExpenseId(-1,3);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }
    //I could not make this test work even though the actual method does work. If someone can spot the mistake
    @Test
    void testDeleteExpense(){
        Expense toDelete = new Expense(event1,"d", Type.Drinks, new Date(), 1.0, p1, true);
        toDelete.expenseId = 4;
        expenseRepository.expenses.add(toDelete);
        ResponseEntity<Expense> response = sut.deleteExpenseByEventIdAndExpenseId(1,4);
        assertEquals(toDelete, response.getBody());
        assertEquals(OK, response.getStatusCode());
        assertEquals("delete",expenseRepository.methods.getLast());
        //cleanup
        expenseRepository.expenses.removeLast();
    }

    @Test
    void testWS(){
        Date date = new Date();
        var actual =
            sut.expenseByEventWS(new ExpenseDTO(1, "", Type.Drinks,
                date, 0.0, "uuid",true));
        assertEquals(new Expense(event1, "", Type.Drinks,
                date, 0.0, p1,true),
            actual.getBody());
        assertEquals("save", expenseRepository.methods.getLast());
        //cleanup
        expenseRepository.expenses.removeLast();
    }
}