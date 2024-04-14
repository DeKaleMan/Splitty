package commons;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ExpenseTest {
    Event event = spy(new Event("test",new Date(10, 10, 2005),"owner","desc"));
    Participant participant = new Participant("test", 10.0,"IBAN","BIC","accountHolder","","email",event);
    Date date = new Date(1,1,1);
    Tag foodTag = new Tag(event, "food", "Random Colour");
    Expense e1;
    Expense e2;

    Expense e3;
    Expense e4;

    @BeforeEach
    void setup(){
        event.id = 1;
        e1 = new Expense(event
            , "food1"
            , foodTag
            , date
            , 150.00
            , participant
            ,true);

        e2 = new Expense(event
            , "food1"
            , foodTag
            , date
            , 150.00
            , participant,
            true);
        e3 = new Expense(event
            , "food2"
            , foodTag
            , date
            , 200.00
            , participant,
            false);
        e4 = new Expense();
        e1.expenseId = 1;
        e2.expenseId = 1;
        e3.expenseId = 3;
        e4.expenseId = 4;
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
    public void getEventTest() {
        assertEquals(event,e1.getEvent());
    }

    @Test
    public void getPayerTest() {
        assertEquals(participant, e1.getPayer());
    }

    @Test
    public void equalsNull2() {
        assertNotEquals(null, e1);
    }

    @Test
    void getType() {
        assertEquals(foodTag, e1.getTag());
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

    @Test
    void getIsShared(){
        assertTrue(e1.isSharedExpense());
    }

    @Test
    void updateTest(){
        e1.updateLastActivity();

        verify(event).updateActivityDate();
    }

    @Test
    void setShared(){
        e1.setSharedExpense(false);
        assertFalse(e1.isSharedExpense());
    }

    @Test
    void setDesc(){
        e1.setDescription("descOther");
        assertEquals("descOther", e1.getDescription());
    }

    @Test
    void setTag(){
        Tag tag = new Tag();
        e1.setTag(tag);
        assertSame(tag,e1.getTag());
    }

    @Test
    void setDate(){
        Date d = new Date();
        e1.setDate(d);
        assertEquals(d, e1.getDate());
    }

    @Test
    void setTotalExpense(){
        e1.setTotalExpense(1.0);
        assertEquals(1.0, e1.getTotalExpense());
    }

    @Test
    void setPayer(){
        Participant p = new Participant();
        e1.setPayer(p);
        assertSame(p,e1.getPayer());
    }

    @Test
    void getExpenseId(){
        assertEquals(1, e1.getExpenseId());
    }

    @Test
    void testNotEqual(){
        assertNotEquals(e1,e3);
    }

    @Test
    void toStringTest(){
        assertEquals("This is an expense:\n" +
            "food1\n" +
            "The expense type is: food.\n" +
            "The total amount spent is: 150.0.\n" +
            "The person who paid was: email, on " + new Date(1,1,1) +".", e1.toString());
    }
}
