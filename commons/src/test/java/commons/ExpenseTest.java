package commons;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTest {

    Transaction t1 = new Transaction("John", 100.50);
    Transaction t2 = new Transaction("Cyndia", -20.50);
    Transaction t22 = new Transaction("Cyndia", -20.50);
    Transaction t3 = new Transaction("Nikolay", -10.0);
    Transaction t4 = new Transaction("Robin", -40.0);
    Transaction t5 = new Transaction("Marshall", -5.0);
    Transaction t6 = new Transaction("Jakub", -25.0);
    List<Transaction> transactions = List.of(t1, t2, t3, t4, t5, t6);
    List<Transaction> transactionsMinusOne = List.of(t1, t2, t3, t4, t5);
    Expense expense = new Expense(1,"This is a test expense", Type.Food,
        Currency.EUR, "24-02-2024", 100.50, "email1");

    Expense expenseCopy = new Expense(1,"This is a test expense", Type.Food,
        Currency.EUR, "24-02-2024", 100.50, "email1");
    Expense expense2 = new Expense(2,"This is a test expense", Type.Drinks,
        Currency.EUR, "24-02-2024", 120.50, "email2");

    @Test
    public void stringTest() {
        String s = "This is an expense:\n" +
            "This is a test expense\n" +
            "The expense type is: Food.\n" +
            "The total amount spent is: 100.5.\n" +
            "The person who paid was: email1, on 24-02-2024 and paid in EUR.";
        assertEquals(s, expense.toString());
    }

    @Test
    public void equals1() {
        assertEquals(t2, t22);
    }

    @Test
    public void equals2() {
        assertEquals(expense, expenseCopy);
    }


    @Test
    public void notEquals1() {
        assertNotEquals(t1, t2);
    }

    @Test
    public void notEquals2() {
        assertNotEquals(expense, expense2);
    }

    @Test
    public void equalsNull1() {
        assertNotEquals(null, t5);
    }

    @Test
    public void equalsNull2() {
        assertNotEquals(null, expense);
    }

    @Test
    public void equalsOtherClass() {
        assertNotEquals(expense, t1);
    }

    @Test
    void testHashCode(){
        assertEquals(expense.hashCode(), expenseCopy.hashCode());
    }


    @Test
    void getType() {
        assertEquals(Type.Food, expense.getType());
    }

    @Test
    void getCurrency() {
        assertEquals(Currency.EUR, expense.getCurrency());
    }

    @Test
    void getDate() {
        assertEquals("24-02-2024",expense.getDate());
    }

    @Test
    void getTotalExpense() {
        assertEquals(100.50,expense.getTotalExpense(),0.001);
    }

    @Test
    void getPayerEmail() {
        assertEquals("email1",expense.getPayerEmail());
    }

    @Test
    void getEventCode() {
        assertEquals(1,expense.getEventCode());
    }

    @Test
    void getDescription(){
        assertEquals("This is a test expense",expense.getDescription());
    }
}
