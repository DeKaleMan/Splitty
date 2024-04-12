package commons.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagDTOTest {

    TagDTO t1;
    TagDTO t2;
    TagDTO t3;
    TagDTO t4;

    @BeforeEach
    void setup() {
        t1 = new TagDTO(1, "name", "colour");
        t2 = new TagDTO(1, "name", "colour");
        t3 = new TagDTO(2, "name1", "colour1");
        t4 = new TagDTO();
    }

    @Test
    void getEventId() {
        assertEquals(1, t1.getEventId());
    }

    @Test
    void setEventId() {
        t1.setEventId(2);
        assertEquals(2, t1.getEventId());
    }

    @Test
    void getName() {
        assertEquals("name", t1.getName());
    }

    @Test
    void setName() {
        t1.setName("nameOther");
        assertEquals("nameOther", t1.getName());
    }

    @Test
    void getColour() {
        assertEquals("colour", t1.getColour());
    }

    @Test
    void setColour() {
        t1.setColour("colourOther");
        assertEquals("colourOther", t1.getColour());
    }

    @Test
    void testEquals() {
        assertEquals(t1, t2);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(t1, t3);
    }

    @Test
    void testHashCode() {
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}