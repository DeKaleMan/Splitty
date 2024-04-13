package commons.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantDTOTest {

    ParticipantDTO p1;
    ParticipantDTO p2;
    ParticipantDTO p3;
    ParticipantDTO p4;

    @BeforeEach
    void setup() {
        p1 = new ParticipantDTO("test", 0.0, "iban", "bic", "email",
            "acc", 1, "uuid");
        p2 = new ParticipantDTO("test", 0.0, "iban", "bic", "email",
            "acc", 1, "uuid");
        p3 =
            new ParticipantDTO("test1", 1.0, "iban1", "bic1", "email1",
                "acc1", 2, "uuid1", "join");
        p4 = new ParticipantDTO();
    }

    @Test
    void getName() {
        assertEquals("test", p1.getName());
    }

    @Test
    void setName() {
        p1.setName("testOther");
        assertEquals("testOther", p1.getName());
    }

    @Test
    void getBalance() {
        assertEquals(0.0, p1.getBalance(), 0.001);
    }

    @Test
    void setBalance() {
        p1.setBalance(2.0);
        assertEquals(2.0, p1.getBalance());
    }

    @Test
    void getIBAN() {
        assertEquals("iban", p1.getiBan());
    }

    @Test
    void setIBAN() {
        p1.setiBan("ibanOther");
        assertEquals("ibanOther", p1.getiBan());
    }

    @Test
    void getbIC() {
        assertEquals("bic", p1.getbIC());
    }

    @Test
    void setbIC() {
        p1.setbIC("bicOther");
        assertEquals("bicOther", p1.getbIC());
    }

    @Test
    void getEmail() {
        assertEquals("email", p1.getEmail());
    }

    @Test
    void setEmail() {
        p1.setEmail("emailOther");
        assertEquals("emailOther", p1.getEmail());
    }

    @Test
    void getAccountHolder() {
        assertEquals("acc", p1.getAccountHolder());
    }

    @Test
    void setAccountHolder() {
        p1.setAccountHolder("accOther");
        assertEquals("accOther", p1.getAccountHolder());
    }

    @Test
    void getEventId() {
        assertEquals(1, p1.getEventId());
    }

    @Test
    void setEventId() {
        p1.setEventId(2);
        assertEquals(2, p1.getEventId());
    }

    @Test
    void getUuid() {
        assertEquals("uuid", p1.getUuid());
    }

    @Test
    void setUuid() {
        p1.setUuid("uuidOther");
        assertEquals("uuidOther", p1.getUuid());
    }

    @Test
    void getEventInviteCode() {
        assertEquals("join", p3.getEventInviteCode());
    }

    @Test
    void getEventInviteCodeNull() {
        assertNull(p1.getEventInviteCode());
    }

    @Test
    void setEventInviteCode() {
        p1.setEventInviteCode("joinOther");
        assertEquals("joinOther", p1.getEventInviteCode());
    }

    @Test
    void isGhost() {
        assertFalse(p1.isGhost());
    }

    @Test
    void setGhostStatus() {
        p1.setGhostStatus(true);
        assertTrue(p1.isGhost());
    }

    @Test
    void isInactive() {
        assertFalse(p1.isInactive());
    }

    @Test
    void setInactive() {
        p1.setInactive(true);
        assertTrue(p1.isInactive());
    }
}