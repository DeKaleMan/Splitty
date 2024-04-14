package commons.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DebtDTOTest {

    DebtDTO d1;

    DebtDTO d2;

    DebtDTO d3;

    DebtDTO d4;


    @BeforeEach
    void setup() {
        d1 = new DebtDTO(1.0, 1, 1, "test");
        d2 = new DebtDTO(1.0, 1, 1, "test");
        d3 = new DebtDTO(2.0, 2, 2, "testOther");
        d4 = new DebtDTO();
    }

    @Test
    void getBalance() {
        assertEquals(1.0, d1.getBalance(), 0.001);
    }

    @Test
    void getEventId() {
        assertEquals(1, d1.getEventId());
    }

    @Test
    void getExpenseId() {
        assertEquals(1, d1.getExpenseId());
    }

    @Test
    void getParticipantUuid() {
        assertEquals("test", d1.getParticipantUuid());
    }

    @Test
    void setEventId() {
        d1.setEventId(2);
        assertEquals(2, d1.getEventId());
    }

    @Test
    void setExpenseId() {
        d1.setExpenseId(2);
        assertEquals(2, d1.getExpenseId());
    }

    @Test
    void testEquals() {
        assertEquals(d1, d2);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(d1, d3);
    }

    @Test
    void testHashCode() {
        assertEquals(d1.hashCode(), d2.hashCode());
    }
}