package server.api;

import commons.Currency;
import commons.Expense;
import commons.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
            sut.saveExpense(new Expense(1, "", Type.Drinks, Currency.EUR,
                null, 0.0, "email"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveEmail() {
        var actual = sut.saveExpense(new Expense(1, "", Type.Drinks,
            Currency.EUR, "date", 0.0, null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void nullSaveNegative() {
        var actual =
            sut.saveExpense(new Expense(1, "", Type.Drinks, Currency.EUR,
                "date", -1.0, "email"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void emptyDate() {
        var actual = sut.saveExpense(new Expense(1, "", Type.Drinks,
            Currency.EUR, "", 0.0, "email"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void emptyEmail() {
        var actual = sut.saveExpense(new Expense(1, "", Type.Drinks,
            Currency.EUR, "date", 0.0, ""));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void testSave() {
        var actual =
            sut.saveExpense(new Expense(1, "", Type.Drinks, Currency.EUR,
                "date", 0.0, "email"));
        assertEquals(new Expense(1, "", Type.Drinks, Currency.EUR,
                "date", 0.0, "email"),
            actual.getBody());
        assertEquals("save", repo.methods.getLast());
    }

    @Test
    void testGetByEventCode() {
        var e1 = new Expense(1, "", Type.Drinks, Currency.EUR,
            "date", 0.0, "email");
        var e2 = new Expense(1, "", Type.Drinks, Currency.EUR,
            "date", 0.0, "testEmail");
        var e3 = new Expense(2, "", Type.Drinks, Currency.EUR,
            "date", 0.0, "email");
        List<Expense> expected = List.of(e1, e2);
        repo.expenses.add(e1);
        repo.expenses.add(e2);
        repo.expenses.add(e3);
        var actual = sut.findByEventCode(1);
        assertEquals(expected, actual);
        assertEquals("findByEventCode", repo.methods.getLast());
    }

    @Test
    void testGetByEventCodeAndPayerEmail() {
        var e1 = new Expense(1, "", Type.Drinks, Currency.EUR,
            "date", 0.0, "email");
        var e2 = new Expense(1, "", Type.Drinks, Currency.EUR,
            "date", 0.0, "testEmail");
        var e3 = new Expense(2, "", Type.Drinks, Currency.EUR,
            "date", 0.0, "email");
        List<Expense> expected = List.of(e1);
        repo.expenses.add(e1);
        repo.expenses.add(e2);
        repo.expenses.add(e3);
        var actual = sut.findByEventCodeAndPayerEmail(1, "email");
        assertEquals(expected, actual);
        assertEquals("findByEventCodeAndPayerEmail", repo.methods.getLast());
    }
}