package commons.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseDTOTest {

    ExpenseDTO e1;
    ExpenseDTO e2;

    ExpenseDTO e3;

    ExpenseDTO e4;


    @BeforeEach
    void setup() {
        e1 = new ExpenseDTO(1, "desc1", "tag1", "colour1",
            new Date(1, 1, 1), 1.0, "payer1", true);
        e2 = new ExpenseDTO(1, "desc1", "tag1", "colour1",
            new Date(1, 1, 1), 1.0, "payer1", true);
        e3 = new ExpenseDTO(3, "desc3", "tag3", "colour3",
            new Date(3, 3, 3), 3.0, "payer3", false);
        e4 = new ExpenseDTO();
    }

    @Test
    void getTagName() {
        assertEquals("tag1", e1.getTagName());
    }

    @Test
    void setTagName() {
        e1.setTagName("tagOther");
        assertEquals("tagOther", e1.getTagName());
    }

    @Test
    void getTagColour() {
        assertEquals("colour1", e1.getTagColour());
    }

    @Test
    void setTagColour() {
        e1.setTagColour("colourOther");
        assertEquals("colourOther", e1.getTagColour());
    }

    @Test
    void getDescription() {
        assertEquals("desc1", e1.getDescription());
    }

    @Test
    void setDescription() {
        e1.setDescription("descOther");
        assertEquals("descOther", e1.getDescription());
    }

    @Test
    void getDate() {
        assertEquals(new Date(1, 1, 1), e1.getDate());
    }

    @Test
    void setDate() {
        Date d = new Date(2, 2, 2);
        e1.setDate(d);
        assertEquals(d, e1.getDate());
    }

    @Test
    void getTotalExpense() {
        assertEquals(1.0, e1.getTotalExpense());
    }

    @Test
    void setTotalExpense() {
        e1.setTotalExpense(2.0);
        assertEquals(2.0, e1.getTotalExpense());
    }

    @Test
    void getPayerUuid() {
        assertEquals("payer1", e1.getPayerUuid());
    }

    @Test
    void setPayerUuid() {
        e1.setPayerUuid("payerOther");
        assertEquals("payerOther", e1.getPayerUuid());
    }

    @Test
    void getEventId() {
        assertEquals(1, e1.getEventId());
    }

    @Test
    void setSharedExpense() {
        e1.setSharedExpense(false);
        assertFalse(e1.isSharedExpense());
    }

    @Test
    void setEventId() {
        e1.setEventId(2);
        assertEquals(2, e1.getEventId());
    }

    @Test
    void isSharedExpense() {
        assertTrue(e1.isSharedExpense());
    }

    @Test
    void testEquals() {
        assertEquals(e1, e2);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(e1, e3);
    }

    @Test
    void testHashCode() {
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("This is an expense:\n" +
            "desc1\n" +
            "The expense type is: tag1.\n" +
            "The total amount spent is: 1.0.\n" +
            "The person who paid was: payer1, on " + new Date(1, 1, 1) + ".", e1.toString());
    }
}