package commons;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTest {
    Event event = new Event("test","date","owner","desc");
    Date date = new Date(1,1,1);
    Expense e1 = new Expense(event
            , "food1"
            , Type.Food
            , Currency.EUR
            , date
            , 150.00
            , "test@student.lentiz.nl");
    Expense e2 = new Expense(event
            , "food1"
            , Type.Food
            , Currency.EUR
            , date
            , 150.00
            , "test@student.lentiz.nl");

    @BeforeEach
    void setup(){
        event.id = 1;
    }

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
        assertEquals(event,e1.getEvent());
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
        assertEquals(date,e1.getDate());
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
