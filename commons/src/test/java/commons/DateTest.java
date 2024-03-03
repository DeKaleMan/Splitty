package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateTest {

    Date date1;
    Date date2;
    Date date3;
    Date date4;
    Date date5;

    @BeforeEach
    void setup(){
        date1 = new Date(1,2,2001);
        date2 = new Date("1.02.2001");
        date3 = new Date("1/02/2001");
        date4 = new Date("1-02-2001");
        date5 = new Date(2,10,2001);
    }

    @Test
    void getDay() {
        assertEquals(1,date1.getDay());
    }

    @Test
    void getMonth() {
        assertEquals(2,date1.getMonth());
    }

    @Test
    void getYear() {
        assertEquals(2001,date1.getYear());
    }

    @Test
    void compareTo() {
        assertTrue(date1.compareTo(date5) < 0);
    }

    @Test
    void testEquals1() {
        assertEquals(date1,date2);
    }
    @Test
    void testEquals2() {
        assertEquals(date2,date3);
    }
    @Test
    void testEquals3() {
        assertEquals(date3,date4);
    }
    @Test
    void testNotEquals() {
        assertNotEquals(date4,date5);
    }

    @Test
    void testHashCode() {
        assertEquals(date1.hashCode(), date2.hashCode());
    }

    @Test
    void testToString1() {
        assertEquals("1-02-2001",date1.toString());
    }

    @Test
    void testToString2() {
        assertEquals("2-10-2001",date5.toString());
    }
}