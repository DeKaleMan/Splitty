package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DebtTest {

    double b1 = 100.50;
    double b2 = -20.00;
    Expense e1 = new Expense(100, "food1", Type.Food, Currency.EUR, "16 March", 150.00, "test@student.lentiz.nl");
    Participant p1 = new Participant("stijn", -500.00, "NL83 INGB 0743 7843", "9843", "R.B. de Jong", "test@gmail.com");
    Debt t1 = new Debt(e1, b1, p1);
    Debt t2 = new Debt(e1, b1, p1);

    @Test
    public void equalsTest(){
        assertEquals(t1, t2);
    }

    @Test
    public void isPositive(){
        assertTrue(t1.isPositive());
    }

    @Test
    public void getBalanceTest(){
        double balance = 100.50;
        assertEquals(t1.getBalance(), balance);
    }

    @Test
    public void getExpAddEventTest(){
        assertEquals(e1, t1.getExpense());
    }

    @Test
    public void setBalanceTest(){
        t1.setBalance(b2);
        assertEquals(-20.00, t1.getBalance());
    }

    @Test
    public void hashcodeTest(){
        assertEquals(t1.hashCode(), t2.hashCode());
    }

}
