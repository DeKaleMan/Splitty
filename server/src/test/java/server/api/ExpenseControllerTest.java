package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class ExpenseControllerTest {
    private ExpenseController sut;
    private TestExpenseRepository expenseRepository;
    private TestEventRepository eventRepository;

    Event event1 = new Event("test","date","owner","desc");
    Event event2 = new Event("test1","date1","owner1","desc1");

    @BeforeEach
    void setup() {
        eventRepository = new TestEventRepository();
        expenseRepository = new TestExpenseRepository();
        sut = new ExpenseController(expenseRepository,eventRepository);
        event1.id=1;
        event2.id=2;
        eventRepository.events.add(event1);
        eventRepository.events.add(event2);
    }

    @Test
    void nullSave() {
        var actual = sut.saveExpense(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveDate() {
        var actual =
            sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks, Currency.EUR,
                null, 0.0, "email"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveEmail() {
        var actual = sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks,
            Currency.EUR, new Date(), 0.0, null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveNegative() {
        var actual =
            sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks, Currency.EUR,
                new Date(), -1.0, "email"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void emptyEmail() {
        var actual = sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks,
            Currency.EUR, new Date(), 0.0, ""));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void testSave() {
        Date date = new Date();
        var actual =
            sut.saveExpense(new ExpenseDTO(1, "", Type.Drinks, Currency.EUR,
                date, 0.0, "email"));
        assertEquals(new Expense(event1, "", Type.Drinks, Currency.EUR,
                date, 0.0, "email"),
            actual.getBody());
        assertEquals("save", expenseRepository.methods.getLast());
    }

    @Test
    void testGetByEventCode() {
        Date date = new Date();
        var e1 = new Expense(event1, "", Type.Drinks, Currency.EUR,
            date, 0.0, "email");
        var e2 = new Expense(event1, "", Type.Drinks, Currency.EUR,
            date, 0.0, "testEmail");
        var e3 = new Expense(event2, "", Type.Drinks, Currency.EUR,
            date, 0.0, "email");
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
        var e1 = new Expense(event1, "", Type.Drinks, Currency.EUR,
            date, 0.0, "email");
        var e2 = new Expense(event1, "", Type.Drinks, Currency.EUR,
            date, 0.0, "testEmail");
        var e3 = new Expense(event2, "", Type.Drinks, Currency.EUR,
            date, 0.0, "email");
        List<Expense> expected = List.of(e1);
        expenseRepository.expenses.add(e1);
        expenseRepository.expenses.add(e2);
        expenseRepository.expenses.add(e3);
        var actual = sut.findByEventCodeAndPayerEmail(1, "email").getBody();
        assertEquals(expected, actual);
        assertEquals("findByEventAndPayerEmail", expenseRepository.methods.getLast());
    }
}