package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {

    private Event e;
    private Event e2;
    @BeforeEach
    void setUp() {
        e = new Event("name"
                , new Date(2005, 10, 10)
                , "owner"
                , "food");
        e2 = new Event("name"
                , new Date(2005, 10, 10)
                , "owner"
                , "food");
    }

    @Test
    public void getNameTest(){
        assertEquals("name", e.getName());
    }

    @Test
    public void getDateTest(){
        assertEquals(new Date(2005, 10, 10), e.getDate());
    }

    @Test
    public void getHostTest(){
        assertEquals("owner", e.getHost());
    }

    @Test
    public void getDescriptionTest(){
        assertEquals("food", e.getDescription());
    }

    @Test
    public void setDateTest(){
        Event eventT = new Event();
        Date date = new Date(2005, 10, 10);
        eventT.setDate(date);
        Date dateCheck = new Date(2005, 10, 10);
        assertEquals(date, dateCheck);
    }

    @Test
    public void setHostTest(){
        Event eventT = new Event();
        String owner = "stijn";
        eventT.setOwner(owner);
        String ownerCheck = eventT.getHost();
        assertEquals(owner, ownerCheck);
    }

    @Test
    public void setDescriptionTest(){
        Event eventT = new Event();
        String desc = "for cinema";
        eventT.setDescription(desc);
        String descCheck = eventT.getDescription();
        assertEquals(desc, descCheck);
    }

    @Test
    public void equalsTest(){
        assertEquals(e,e2);
    }

    @Test
    public void hashcodeTest(){
        assertEquals(e.hashCode(), e2.hashCode());
    }
}
