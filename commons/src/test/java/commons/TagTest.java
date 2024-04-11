package commons;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TagTest {
    Event e1 = new Event("test1",new Date(10, 10, 2005),"owner","desc");
    Event e2 = new Event("test2",new Date(15, 15, 2015),"owner","desc");
    Tag t1 = new Tag(e1, "Food", "#2a8000");
    Tag t2 = new Tag(e1, "Travel", "#3700ff");
    Tag t3 = new Tag(e2, "Entrance Fees", "#c50000");
    @Test
    public void equalsTestTrue() {
        assertEquals(t1, new Tag(e1, "Food", "c50000"));
    }
    @Test
    public void equalsTestFalse() {
        assertNotEquals(t1, new Tag(e1, "Travel", "2a8000"));
        assertNotEquals(t1, new Tag(e2, "Food", "2a8000"));
        assertNotEquals(t3, t2);
    }
    @Test
    public void getterSetterTest() {
        assertEquals("Travel", t2.getName());
        assertEquals("#c50000",t3.getColour());
        assertEquals(e1, t1.getEvent());
        Tag temp = new Tag();
        temp.setEvent(e2);
        temp.setName("Other");
        temp.setColour("#525252FF");
        assertEquals("Other",temp.getName());
        assertEquals("#525252FF",temp.getColour());
        assertEquals(e2, temp.getEvent());
        temp.setTagId(t3.getTagId());
        assertEquals(t3, temp);
    }
}
