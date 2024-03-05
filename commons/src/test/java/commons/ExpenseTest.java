package commons;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTest {
    Expense e1 = new Expense(100
            , "food1"
            , Type.Food
            , Currency.EUR
            , "16 March"
            , 150.00
            , "test@student.lentiz.nl");
    Expense e2 = new Expense(100
            , "food1"
            , Type.Food
            , Currency.EUR
            , "16 March"
            , 150.00
            , "test@student.lentiz.nl");

    @Test
    public void equalsTest(){
        assertEquals(e1, e2);
    }

    @Test
    public void hashcodeTest() {
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    public void getEventcodeTest() {
        assertEquals(100, e1.getEventCode());
    }

    @Test
    public void getPayerEmailTest() {
        assertEquals("test@student.lentiz.nl", e1.getPayerEmail());
    }

    @Test
    public void equalsNull2() {
        assertNotEquals(null, e1);
    }

    @Test
    void getType() {
        assertEquals(Type.Food, e1.getType());
    }

    @Test
    void getCurrency() {
        assertEquals(Currency.EUR, e1.getCurrency());
    }

    @Test
    void getDate() {
        assertEquals("16 March",e1.getDate());
    }

    @Test
    void getTotalExpense() {
        assertEquals(150.00,e1.getTotalExpense(),0.001);
    }


    @Test
    void getDescription(){
        assertEquals("food1",e1.getDescription());
    }
}
