package server.api;

import commons.*;
import commons.dto.ExpenseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import server.service.ExpenseService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

class ExpenseControllerTest {
    @Mock
    ExpenseService service;
    @InjectMocks
    private ExpenseController sut;

    Event event1 = new Event("test", new Date(10, 10, 2005), "owner", "desc");
    Event event2 = new Event("test1", new Date(10, 10, 2005), "owner1", "desc1");

    Participant p1 = new Participant("test", 10.0, "IBAN", "BIC", "email", "", "uuid", event1);

    Participant p2 = new Participant("test", 10.0, "IBAN", "BIC", "email1", "", "uuid2", event1);

    Participant p3 = new Participant("test", 10.0, "IBAN", "BIC", "email3", "", "uuid3",event2);
    Tag t1 = new Tag(event1, "Food", "2a8000");
    Tag t2 = new Tag(event2, "Travel", "3700ff");
    Date date = new Date();
    Expense e1 = new Expense(event1, "", t1,
        date, 0.0, p1,true);
    Expense e2 = new Expense(event1, "", t1,
        date, 0.0, p2,true);
    Expense e3 = new Expense(event2, "", t2,
        date, 0.0, p1,true);


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        event1.id = 1;
        event2.id = 2;
        e1.expenseId = 1;
        e2.expenseId = 2;
        e3.expenseId = 3;
    }

    @Test
    void testInvalidSave() {
        when(service.saveExpense(any(ExpenseDTO.class))).thenReturn(null);
        var actual = sut.saveExpense(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void testSave() {
        Date date = new Date();
        Expense expected = new Expense(event1, "", t1,
                date, 0.0, p1, true);
        when(service.saveExpense(any(ExpenseDTO.class))).thenReturn(expected);
        var actual =
                sut.saveExpense(new ExpenseDTO(1, "", t1.getName(), t1.getColour(),
                        date, 0.0, "uuid", true));
        assertEquals(expected, actual.getBody());
        assertEquals(OK, actual.getStatusCode());

    }


    @Test
    void testGetByEventCode() {
        List<Expense> expected = List.of(e1, e2);
        when(service.getByEventCode(anyInt())).thenReturn(expected);
        var actual = sut.findByEventCode(1);
        assertEquals(expected, actual.getBody());
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    void testGetByEventCodeInvalid() {
        when(service.getByEventCode(anyInt())).thenReturn(null);
        ResponseEntity<List<Expense>> response = sut.findByEventCode(-1);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetByEventCodeAndPayerUuid() {
        List<Expense> expected = List.of(e1);
        when(service.getByEventCodeAndPayerUuid(anyInt(), anyString())).thenReturn(expected);
        var actual = sut.findByEventCodeAndPayerUuid(1, "uuid");
        assertEquals(OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void testGetByEventCodeAndPayerUuidInvalid() {
        when(service.getByEventCodeAndPayerUuid(anyInt(), anyString())).thenReturn(null);
        ResponseEntity<List<Expense>> response = sut.findByEventCodeAndPayerUuid(-1, "invalid");
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdate() {
        Expense updated = new Expense(event1, "d2", Type.Food, new Date(1, 1, 1), 2.0,
                p2, false);
        updated.expenseId = 4;
        when(service.updateExpense(anyInt(), anyInt(), any(ExpenseDTO.class))).thenReturn(updated);
        ResponseEntity<Expense> response = sut.updateExpense(1, 4, new ExpenseDTO(1, "d2",
                Type.Food, new Date(1, 1, 1), 2.0, "uuid2", false));
        assertEquals(updated, response.getBody());
        assertEquals(OK, response.getStatusCode());
    }


    @Test
    void testUpdateInvalidEvent() {
        when(service.updateExpense(anyInt(), anyInt(), any(ExpenseDTO.class))).thenReturn(null);
        ResponseEntity<Expense> response = sut.updateExpense(-1, 1, new ExpenseDTO());
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteInvalid() {
        when(service.deleteExpense(anyInt(), anyInt())).thenReturn(null);
        ResponseEntity<Expense> response = sut.deleteExpenseByEventIdAndExpenseId(-1, -1);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    //I could not make this test work even though the actual method does work. If someone can spot the mistake
    @Test

    void testDeleteExpense() {
        Expense toDelete = new Expense(event1, "d", t1, new Date(), 1.0, p1, true);
        toDelete.expenseId = 4;
        when(service.deleteExpense(anyInt(), anyInt())).thenReturn(toDelete);
        ResponseEntity<Expense> response = sut.deleteExpenseByEventIdAndExpenseId(1, 4);
        assertEquals(toDelete, response.getBody());
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    void testUpdateWS() {
        Date date = new Date();
        Expense e = new Expense(event1, "", t1,
            date, 0.0, p1, true);
        var actual =
            sut.updateExpenseWS(e);
        assertEquals(e,
            actual);
    }

    @Test
    void testDeleteWS() {
        Date date = new Date();
        Expense e = new Expense(event1, "", t1,
            date, 0.0, p1, true);
        var actual =
            sut.deleteExpenseWS(e);
        assertEquals(e,
            actual);
    }
}