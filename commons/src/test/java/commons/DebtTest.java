package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Date;

public class DebtTest {

    Event e1 = new Event("test1", new Date(10), "test1","test1");
    Event e2 = new Event("test2", new Date(20), "test2","test2");

    Participant p1 = new Participant("test1", 10.0, "test1", "test1", "test1", "","sss", e1);

    Participant p2 = new Participant("test2", 11.0, "test2", "test2", "test2", "test2", "d",e2);

    Expense expense1 = new Expense(e1, "test1", Type.Food, new Date(10), 10.0,p1);

    Expense expense2 = new Expense(e2, "test1", Type.Food, new Date(10), 1.0,p2);

    Debt d1 = new Debt(expense1,10.0,p1);
    Debt d1Copy = new Debt(expense1,10.0,p1);

    Debt d2 = new Debt(expense2,1.0,p2);

    Debt emptyDebt = new Debt();

    @Test
    void getBalance() {
        assertEquals(10.0,d1.getBalance());
    }

    @Test
    void getExpense() {
        assertEquals(expense1,d1.getExpense());
    }

    @Test
    void setBalance() {
        Debt d = new Debt(expense1,10.0,p1);
        d.setBalance(20.0);
        assertEquals(20.0,d.getBalance());
    }


    @Test
    void getParticipant() {
        assertEquals(p1,d1.getParticipant());
    }

    @Test
    void getId(){
        d1.id = 1;
        assertEquals(1,d1.getId());
    }

    @Test
    void testEquals() {
        assertEquals(d1,d1Copy);
    }

    @Test
    void testNotEqual(){
        assertNotEquals(d1,d2);
    }

    @Test
    void testHashCode() {
        assertEquals(d1.hashCode(),d1Copy.hashCode());
    }
}
