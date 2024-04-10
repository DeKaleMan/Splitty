package service;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import commons.dto.ExpenseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestEventRepository;
import server.api.TestExpenseRepository;
import server.api.TestParticipantRepository;
import server.service.ExpenseService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseServiceTest {
    private ExpenseService sut;
    private TestExpenseRepository expenseRepository;
    private TestEventRepository eventRepository;
    private TestParticipantRepository participantRepository;

    Event event1 = new Event("test", new Date(10, 10, 2005), "owner", "desc");
    Event event2 = new Event("test1", new Date(10, 10, 2005), "owner1", "desc1");

    Participant p1 = new Participant("test", 10.0, "IBAN", "BIC", "email", "", "uuid", event1);

    Participant p2 = new Participant("test", 10.0, "IBAN", "BIC", "email1", "", "uuid2", event1);

    Participant p3 = new Participant("test", 10.0, "IBAN", "BIC", "email3", "", "uuid3", event2);

    Date date = new Date();
    Tag t1 = new Tag(event1, "Food", "#2a8000");
    Tag t2 = new Tag(event1, "Travel", "#3700ff");
    Tag t3 = new Tag(event2, "Entrance Fees", "#c50000");
    Expense e1 = new Expense(event1, "", t1,
        date, 0.0, p1, true);
    Expense e2 = new Expense(event1, "", t2,
        date, 0.0, p2, true);
    Expense e3 = new Expense(event2, "", t3,
        date, 0.0, p1, true);

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        expenseRepository = new TestExpenseRepository();
        participantRepository = new TestParticipantRepository();
        sut = new ExpenseService(expenseRepository, eventRepository, participantRepository);
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
        assertNull(actual);
    }

    @Test
    void nullSaveDate() {
        var actual =
            sut.saveExpense(new ExpenseDTO(1, "", t1.getName(), t1.getColour(),
                null, 0.0, "uuid", true));
        assertNull(actual);
    }

    @Test
    void nullSaveUuid() {
        var actual = sut.saveExpense(new ExpenseDTO(1, "", t1.getName(), t1.getColour(),
            new Date(), 0.0, null, true));
        assertNull(actual);
    }

    @Test
    void nullSaveNegative() {
        var actual =
            sut.saveExpense(new ExpenseDTO(1, "", t1.getName(), t1.getColour(),
                new Date(), -1.0, "uuid", true));
        assertNull(actual);
    }

    @Test
    void emptyUuid() {
        var actual = sut.saveExpense(new ExpenseDTO(1, "", t1.getName(), t1.getColour(),
            new Date(), 0.0, "", true));
        assertNull(actual);
    }

    @Test
    void testSave() {
        Date date = new Date();
        var actual =
            sut.saveExpense(new ExpenseDTO(1, "", t1.getName(), t1.getColour(),
                date, 0.0, "uuid", true));
        assertEquals(new Expense(event1, "", t1,
                date, 0.0, p1, true),
            actual);
        assertEquals("save", expenseRepository.methods.getLast());
        //cleanup
        expenseRepository.expenses.removeLast();
    }

    @Test
    void testSaveInvalidEvent() {
        Expense actual = sut.saveExpense(new ExpenseDTO(-1, "", t1.getName(), t1.getColour(),
            date, 0.0, "uuid", true));
        assertNull(actual);
    }

    @Test
    void testGetByEventCode() {
        List<Expense> expected = List.of(e1, e2);
        var actual = sut.getByEventCode(1);
        assertEquals(expected, actual);
        assertEquals("findByEvent", expenseRepository.methods.getLast());
    }

    @Test
    void testGetByEventCodeInvalid() {
        List<Expense> actual = sut.getByEventCode(-1);
        assertNull(actual);
    }

    @Test
    void testGetByEventCodeAndPayerUuid() {
        List<Expense> expected = List.of(e1);
        var actual = sut.getByEventCodeAndPayerUuid(1, "uuid");
        assertEquals(expected, actual);
        assertEquals("findByEventAndPayerUuid", expenseRepository.methods.getLast());
    }

    @Test
    void testGetByEventCodeAndPayerUuidInvalid() {
        List<Expense> actual = sut.getByEventCodeAndPayerUuid(-1, "invalid");
        assertNull(actual);
    }

    @Test
    void testUpdate() {
        Expense toUpdate = new Expense(event1, "d", t1, new Date(), 1.0, p1,
            true);
        toUpdate.expenseId = 4;
        expenseRepository.expenses.add(toUpdate);
        Expense updated = new Expense(event1, "d2", t1, new Date(1, 1, 1), 2.0,
            p2, false);
        updated.expenseId = 4;
        Expense actual = sut.updateExpense(1, 4, new ExpenseDTO(1, "d2",
                t1.getName(), t1.getColour(), new Date(1, 1, 1), 2.0, "uuid2", false));
        assertEquals(updated, actual);
        assertEquals("save", expenseRepository.methods.getLast());
        //cleanup
        expenseRepository.expenses.removeIf(x -> x.expenseId == 4 && x.getEvent().equals(event1));
    }

    @Test
    void testUpdateInvalidEvent() {
        Expense actual = sut.updateExpense(-1, 1, new ExpenseDTO());
        assertNull(actual);
    }

    @Test
    void testUpdateInvalidExpense() {
        Expense actual = sut.updateExpense(1, -1, new ExpenseDTO());
        assertNull(actual);
    }

    @Test
    void testIncorrectDelete1() {
        var actual = sut.deleteExpense(0, 0);
        assertNull(actual);
        Date date = new Date();
        ExpenseDTO e1 = new ExpenseDTO(event1.id, "", t1.getName(), t1.getColour(),
            date, 0.0, "uuid", true);
        var actual2 = sut.deleteExpense(event1.id, -33);
        assertNull(actual);
    }

    @Test
    void testIncorrectDelete2() {
        Expense actual = sut.deleteExpense(-1, 3);
        assertNull(actual);
    }

    //I could not make this test work even though the actual method does work. If someone can spot the mistake
    @Test
    void testDeleteExpense() {
        Expense toDelete = new Expense(event1, "d", t1, new Date(), 1.0, p1, true);
        toDelete.expenseId = 4;
        expenseRepository.expenses.add(toDelete);
        Expense actual = sut.deleteExpense(1, 4);
        assertEquals(toDelete, actual);
        assertEquals("delete", expenseRepository.methods.getLast());
        //cleanup
        expenseRepository.expenses.removeLast();
    }

}