package server.api;

import commons.Currency;
import commons.Expense;
import commons.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.ExpenseRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class ExpenseControllerTest {
    private ExpenseController sut;
    private TestExpenseRepository repo;

    @BeforeEach
    void setup() {
        repo = new TestExpenseRepository();
        sut = new ExpenseController(repo);
    }

    @Test
    void nullSave() {
        var actual = sut.saveExpense(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveDate() {
        var actual =
            sut.saveExpense(new Expense("", Type.Drinks, Currency.EUR,
                null, 0.0, "email"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveEmail() {
        var actual = sut.saveExpense(new Expense("", Type.Drinks, Currency.EUR,
            "date", 0.0, null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveNegative() {
        var actual =
            sut.saveExpense(new Expense("", Type.Drinks, Currency.EUR,
                "date", -1.0, "email"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void emptyDate() {
        var actual = sut.saveExpense(new Expense("", Type.Drinks, Currency.EUR,
            "", 0.0, "email"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void emptyEmail() {
        var actual = sut.saveExpense(new Expense("", Type.Drinks, Currency.EUR,
            "date", 0.0, ""));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void testSave() {
        var actual =
            sut.saveExpense(new Expense("", Type.Drinks, Currency.EUR,
                "date", 0.0, "email"));
        assertEquals(new Expense("", Type.Drinks, Currency.EUR,
                "date", 0.0, "email"),
            actual.getBody());
        assertEquals("save", repo.methods.getLast());
    }

    @Test
    void testGetByEventCode() {

    }
}