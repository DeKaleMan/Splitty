package commons.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDTOTest {

    PaymentDTO p1;

    PaymentDTO p2;

    @BeforeEach
    void setup() {
        p1 = new PaymentDTO("payer", "payee", 1, 10.0, false);
        p2 = new PaymentDTO();
    }

    @Test
    void getPayerUuid() {
        assertEquals("payer", p1.getPayerUuid());
    }

    @Test
    void getPayeeUuid() {
        assertEquals("payee", p1.getPayeeUuid());
    }

    @Test
    void getAmount() {
        assertEquals(10.0, p1.getAmount());
    }

    @Test
    void getEventCode() {
        assertEquals(1, p1.getEventCode());
    }

    @Test
    void isPaid() {
        assertFalse(p1.isPaid());
    }
}