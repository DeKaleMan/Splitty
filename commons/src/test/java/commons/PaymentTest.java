package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    Participant par1;

    Participant par2;

    Payment p1;
    Payment p2;
    Payment p3;

    @BeforeEach
    void setup(){
        par1 = new Participant();
        par2 = new Participant();

        p1 = new Payment(par1, par2, 1.0, false);
        p2 = new Payment(par1, par2, 1.0, true);
        p3 = new Payment();
        p1.id = 1L;
        p2.id = 1L;
    }


    @Test
    void getId() {
        assertEquals(1L, p1.getId());
    }

    @Test
    void setId() {
        p1.setId(2L);
        assertEquals(2L, p1.getId());
    }

    @Test
    void getPayer() {
        assertSame(par1,p1.getPayer());
    }

    @Test
    void setPayer() {
        p1.setPayer(par2);
        assertSame(par2, p1.getPayer());
    }

    @Test
    void getPayee() {
        assertSame(par2,p1.getPayee());
    }

    @Test
    void setPayee() {
        p1.setPayee(par1);
        assertSame(par1, p1.getPayee());
    }

    @Test
    void getAmount() {
        assertEquals(1.0, p1.getAmount());
    }

    @Test
    void setAmount() {
        p1.setAmount(2.0);
        assertEquals(2.0, p1.getAmount());
    }

    @Test
    void isPaid() {
        assertFalse(p1.isPaid());
    }

    @Test
    void setPaid() {
        p1.setPaid(true);
        assertTrue(p1.isPaid());
    }

    @Test
    void testEquals() {
        assertEquals(p1,p2);
    }

    @Test
    void testNotEquals() {
        p2.id = 2L;
        assertNotEquals(p1,p2);
    }

    @Test
    void testHashCode() {
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}