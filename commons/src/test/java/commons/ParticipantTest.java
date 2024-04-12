package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParticipantTest {
    Participant p1;
    Participant p2;
    Participant p3;
    Event e1;
    Event e2;


    // this test is for Participant and ParticipantId

    @BeforeEach
    void setup() {
        e1 = new Event("Party"
                , new Date(2005, 10, 10)
                , ""
                , "The part is at .....");
        e2 = new Event("Swimming pool event"
                , new Date(2005, 10, 10)
                , ""
                , "The swimming pool is open from....");

        p1 = new Participant("John", 0.0, "NLABNA2049250232522", "ABNANL02",
                "john.smith@gmail.com", "John Smith", UUID.randomUUID().toString(), e1);
        p2 = new Participant("Kaitlin", 0.0, "AMCSK2225228939", "OKAMKL34",
                "kaitlin@gmail.com", "Kaitlin", UUID.randomUUID().toString(), e1);
        p3 = new Participant("George", 0.0, "PLESC0252242761032", "PASKEE45",
                "george.smith@gmail.com", "George Smith", UUID.randomUUID().toString(), e2);
        e2.setHost(p3.getUuid());
        e1.setHost(p1.getUuid());
    }

    @Test
    public void equalsTest() {
        assertEquals(p2, new Participant("Johnny", 234.0, "NLABNA2049250232522", "PASKEE45",
                "kaitlin@gmail.com", "Kaitlin", p2.getUuid(), e1));
    }
    @Test
    public void equalsTestFalse() {
        assertNotEquals(p2, new Participant("Kaitlin", 1.0, "AMCSK2225228939", "OKAMKL34",
                "kaitlin@gmail.com", "Kaitlin", p1.getUuid(), e1));
    }

    @Test
    public void hashTestTrue() {
        Participant temp = new Participant("Kaitlin", 0.0, "AMCSK2225228939", "OKAMKL34",
                "kaitlin@gmail.com", "Kaitlin", p2.getUuid(), e1);
        assertEquals(p2.hashCode(), temp.hashCode());
    }
    @Test
    public void hashTestFalse() {
        Participant temp = new Participant("Kaitlin", 0.0, "AMCSK2225228939", "OKAMKL34",
                "kaitlin@gmail.com", "Kaitlin", p2.getUuid(), e2);
        assertNotEquals(p2.hashCode(), temp.hashCode());
    }

    @Test
    public void testLatestActivity() {
        Date date = new Date();
        p3.updateLastActivity();
        assertEquals(date, p3.getEvent().getLastActivity());
    }
    @Test
    public void getterSetterTest() {
        Participant temp = new Participant();
        assertNotEquals(p1, temp);
        temp.setUuid(p1.getUuid());
        temp.setEvent(p1.getEvent());
        assertEquals(p1, temp);
        assertFalse(temp.isGhost());
        temp.setGhost(true);
        temp.setBIC("1234");
        temp.setBalance(100);
        temp.setName("Jojo");
        temp.setIBan("NL1234");
        temp.setEmail("jojo@yahoo.com");
        temp.setAccountHolder("Jojo Franklin");
        assertEquals("1234", temp.getBIC());
        assertEquals("Jojo", temp.getName());
        assertEquals("NL1234", temp.getIBan());
        assertEquals("Jojo Franklin", temp.getAccountHolder());
        assertEquals("jojo@yahoo.com", temp.getEmail());
        assertEquals(100.00, temp.getBalance());
        assertTrue(temp.isGhost());
        assertEquals(p1, temp);
        assertEquals(p1.getId(), temp.getId());
    }

}
