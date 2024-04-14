package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseIdTest {
    Event e = new Event();
    ExpenseId e1 = new ExpenseId(e,1);
    ExpenseId e2 = new ExpenseId(e,1);
    ExpenseId e3 = new ExpenseId(e,2);
    ExpenseId e4 = new ExpenseId();

    @Test
    void getEvent() {
        assertEquals(e, e1.getEvent());
    }

    @Test
    void getExpenseId() {
        assertEquals(1, e1.getExpenseId());
    }

    @Test
    void testEquals() {
        assertEquals(e1,e2);
    }

    @Test
    void testNotEquals(){
        assertNotEquals(e1,e3);
    }

    @Test
    void testHashCode() {
        assertEquals(e1.hashCode(), e2.hashCode());
    }
}