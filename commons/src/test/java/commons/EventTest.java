package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EventTest {

    Event e1;
    Event e2;
    Event e3;
    @BeforeEach
    void setUp() {
        e1 = new Event("name"
                , new Date(2005, 10, 10)
                , "owner"
                , "food");
        e1.id = 1;
        e2 = new Event("name"
                , new Date(2005, 10, 10)
                , "owner"
                , "food");
        e2.id = 1;
        e3 = new Event("name1"
            , new Date(2, 2, 2)
            , "owner1"
            , "food1");
        e3.id = 3;
    }

    @Test
    public void getNameTest(){
        assertEquals("name", e1.getName());
    }

    @Test
    void setNameTest(){
        e1.setName("nameOther");
        assertEquals("nameOther", e1.getName());
    }

    @Test
    public void getDateTest(){
        assertEquals(new Date(2005, 10, 10), e1.getDate());
    }

    @Test
    public void getHostTest(){
        assertEquals("owner", e1.getHost());
    }

    @Test
    public void getDescriptionTest(){
        assertEquals("food", e1.getDescription());
    }

    @Test
    public void setDateTest(){
        Event eventT = new Event();
        Date date = eventT.getLastActivity();
        eventT.setDate(date);
        assertEquals(date, eventT.getDate());
    }

    @Test
    public void setHostTest(){
        Event eventT = new Event();
        String owner = "stijn";
        eventT.setHost(owner);
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
    void getIdTest(){
        assertEquals(1, e1.getId());
    }

    @Test
    void setId(){
        e1.setId(2);
        assertEquals(2,e1.getId());
    }

    @Test
    public void equalsTest(){
        assertEquals(e1,e2);
    }

    @Test
    void notEqualsTest(){
        assertNotEquals(e1,e3);
    }

    @Test
    void inviteCode(){
        e1.setInviteCode("join");
        assertEquals("join", e1.getInviteCode());
    }

    @Test
    public void hashcodeTest(){
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void toStringTest(){
        assertEquals("This is event with name:" +
            " name that is created on " +
            new Date(2005,10,10) +
            " the person that created is: owner" +
            " the description is: food", e1.toString());
    }
}
