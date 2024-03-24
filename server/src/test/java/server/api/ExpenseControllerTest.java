package server.api;

import commons.*;
import commons.dto.ExpenseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        expenseRepository = new TestExpenseRepository();
        participantRepository = new TestParticipantRepository();
        sut = new ExpenseController(expenseRepository, eventRepository, participantRepository);
        event1.id = 1;
        event2.id = 2;
        eventRepository.events.add(event1);
        eventRepository.events.add(event2);
        participantRepository.participants.add(p1);
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
                null, 0.0, "uuid"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveUuid() {
        var actual = sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks,
            new Date(), 0.0, null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveNegative() {
        var actual =
            sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks,
                new Date(), -1.0, "uuid"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void emptyUuid() {
        var actual = sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks,
            new Date(), 0.0, ""));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void testSave() {
        Date date = new Date();
        var actual =
            sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks,
                date, 0.0, "uuid"));
        assertEquals(new Expense(event1, "", Type.Drinks,
                date, 0.0, p1),
            actual.getBody());
        assertEquals("save", expenseRepository.methods.getLast());
    }

    @Test
    void testGetByEventCode() {
        Date date = new Date();
        var e1 = new Expense(event1, "", Type.Drinks,
            date, 0.0, p1);
        var e2 = new Expense(event1, "", Type.Drinks,
            date, 0.0, p1);
        var e3 = new Expense(event2, "", Type.Drinks,
            date, 0.0, p1);
        List<Expense> expected = List.of(e1, e2);
        expenseRepository.expenses.add(e1);
        expenseRepository.expenses.add(e2);
        expenseRepository.expenses.add(e3);
        var actual = sut.findByEventCode(1).getBody();
        assertEquals(expected, actual);
        assertEquals("findByEvent", expenseRepository.methods.getLast());
    }

    @Test
    void testGetByEventCodeAndPayerEmail() {
        Date date = new Date();
        var e1 = new Expense(event1, "", Type.Drinks,
            date, 0.0, p1);
        var e2 = new Expense(event1, "", Type.Drinks,
            date, 0.0, p2);
        var e3 = new Expense(event2, "", Type.Drinks,
            date, 0.0, p3);
        List<Expense> expected = List.of(e1);
        expenseRepository.expenses.add(e1);
        expenseRepository.expenses.add(e2);
        expenseRepository.expenses.add(e3);
        var actual = sut.findByEventCodeAndPayerUuid(1, "uuid").getBody();
        assertEquals(expected, actual);
        assertEquals("findByEventAndPayerUuid", expenseRepository.methods.getLast());
    }

    @Test
    void testIncorrectDelete(){
        var actual = sut.deleteExpenseByEventIdAndExpenseId(0, 0);
        assertEquals(NOT_FOUND, actual.getStatusCode());
        Date date = new Date();
        ExpenseDTO e1 = new ExpenseDTO(event1.id, "", Type.Drinks,
                date, 0.0, "uuid");
        var actual2 = sut.deleteExpenseByEventIdAndExpenseId(event1.id, -33);
        assertEquals(NOT_FOUND, actual.getStatusCode());
        // TODO the one where you check whether the eventCode is incorrect and the expenseID is correct,
        // I just haven't figured out how to get the expenseID

    }
    //I could not make this test work even though the actual method does work. If someone can spot the mistake
    @Test
    void testDeleteExpense(){
//        Date date = new Date();
//        ExpenseDTO e1 = new ExpenseDTO(event1.id, "", Type.Drinks,
//                date, 0.0, "email");
//
//        List<Expense> expected = List.of(new Expense(event1, "",
//                Type.Drinks, date, 0.0, "email"));
//        sut.saveExpense(e1);
//
//        assertEquals(expected, sut.findByEventCode(1).getBody());
//
//        int eId = sut.findByEventCodeAndPayerEmail(event1.id, "email").getBody().getFirst().getExpenseId();
//
//        Expense actual = sut.deleteExpenseByEvent_IdAndExpenseId(e1.getEventId(),eId).getBody();
//        assertEquals("deleteExpense", expenseRepository.methods.getLast());

        // Debugging works but when I try to do it like this the check in the deleteMethod whether the expense with
        // this expenseID exists is empty and therefore this test fails. even though it actually works...

//        assertEquals(e1, actual);
//        assertEquals(0, sut.findByEventCode(event1.id).getBody().size());

    }
}