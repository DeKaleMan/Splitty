package server.api;

import commons.*;
import commons.dto.DebtDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.DebtService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class DebtControllerTest {

    @Mock
    DebtService service;

    @InjectMocks
    DebtController sut;


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
        MockitoAnnotations.openMocks(this);
        //sut = new DebtController(service);
        e1.id = 1;
        e2.id = 2;

        expense1.expenseId = 1;
        expense2.expenseId = 2;
        expense3.expenseId = 3;
    }


    @Test
    void getAllDebtsOfEvent() {
        List<Debt> expected = List.of(d1, d3);
        when(service.getAllDebtsOfEvent(anyInt())).thenReturn(expected);
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfEvent(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getAllDebtsOfEventInvalid() {
        when(service.getAllDebtsOfEvent(anyInt())).thenReturn(null);
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfEvent(-1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAllDebtsOfExpense() {
        List<Debt> expected = List.of(d1);
        when(service.getAllDebtsOfExpense(anyInt(), anyInt())).thenReturn(expected);
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfExpense(1, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getAllDebtsOfExpenseInvalid() {
        when(service.getAllDebtsOfExpense(anyInt(), anyInt())).thenReturn(null);
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfExpense(-1, -1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAllDebtsOfParticipant() {
        List<Debt> expected = List.of(d1, d3);
        when(service.getAllDebtsOfParticipant(anyInt(), anyString())).thenReturn(expected);
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfParticipant(1, "test1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getAllDebtsOfParticipantInvalid() {
        when(service.getAllDebtsOfParticipant(anyInt(), anyString())).thenReturn(null);
        ResponseEntity<List<Debt>> response = sut.getAllDebtsOfParticipant(-1, "invalid");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void getALlDebts() {
        List<Debt> expected = List.of(d1, d2, d3);
        when(service.getALlDebts()).thenReturn(expected);
        ResponseEntity<List<Debt>> response = sut.getALlDebts();
        assertEquals(expected, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void saveDebt() {
        Debt expected = new Debt(expense1, 25.0, p1);
        when(service.saveDebt(any(DebtDTO.class))).thenReturn(expected);
        ResponseEntity<Debt> response = sut.saveDebt(new DebtDTO(25.0, 1, 1, "test1"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void saveDebtInvalid() {
        when(service.saveDebt(any(DebtDTO.class))).thenReturn(null);
        ResponseEntity<Debt> response = sut.saveDebt(new DebtDTO(25.0, -1, -1, "invalid"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteDebts() {
        List<Debt> expected = List.of(new Debt(expense1, 1, p1), new Debt(expense1, 2, p1));
        when(service.deleteDebtsOfExpense(anyInt(), anyInt())).thenReturn(expected);
        ResponseEntity<List<Debt>> response = sut.deleteDebtsOfExpense(1, 1);
        assertEquals(expected, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteDebtsInvalid() {
        when(service.deleteDebtsOfExpense(anyInt(), anyInt())).thenReturn(null);
        ResponseEntity<List<Debt>> response = sut.deleteDebtsOfExpense(-1, -1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}