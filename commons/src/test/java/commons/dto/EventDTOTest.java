package commons.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EventDTOTest {

    EventDTO e1;
    EventDTO e2;
    EventDTO e3;

    EventDTO e4;

    @BeforeEach
    void setup() {
        e1 = new EventDTO("e1", new Date(1, 1, 1), "owner1", "desc1");
        e2 = new EventDTO("e1", new Date(1, 1, 1), "owner1", "desc1");
        e3 = new EventDTO("e3", new Date(2, 2, 2), "owner3", "desc3");
        e4 = new EventDTO();
    }

    @Test
    void getName() {
        assertEquals("e1", e1.getName());
    }

    @Test
    void getDate() {
        assertEquals(new Date(1, 1, 1), e1.getDate());
    }

    @Test
    void getOwner() {
        assertEquals("owner1", e1.getOwner());
    }

    @Test
    void getDescription() {
        assertEquals("desc1", e1.getDescription());
    }

    @Test
    void setName() {
        e1.setName("eOther");
        assertEquals("eOther", e1.getName());
    }

    @Test
    void setDate() {
        Date d = new Date(3, 3, 3);
        e1.setDate(d);
        assertEquals(d, e1.getDate());
    }

    @Test
    void setOwner() {
        e1.setOwner("ownerOther");
        assertEquals("ownerOther", e1.getOwner());
    }

    @Test
    void setDescription() {
        e1.setDescription("descOther");
        assertEquals("descOther", e1.getDescription());
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
        assertEquals("This is event with name:" +
            " e1 that is created on " +
            new Date(1, 1, 1) +
            " the person that created is: owner1 the description is: desc1", e1.toString());
    }
}