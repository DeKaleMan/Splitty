package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AddTagCtrlTest {
    private AddTagCtrl sut;

    @BeforeEach
    public void setup() {
        sut = new AddTagCtrl(new MainCtrl());
    }

    @Test
    public void colourCheckerTest() {
        String c1 = "";
        String c2 = "#ABC123";
        String c3 = "#123455";
        String c4 = "000000";
        String c5 = "#";
        String c6 = "#AVd257";
        String c7 = "#ABCDE";
        String c8 = "EEE";
        String c9 = "1234";
        String c10 = "#ABC";
        assertTrue(sut.checkColour(c1));
        assertTrue(sut.checkColour(c2));
        assertTrue(sut.checkColour(c3));
        assertFalse(sut.checkColour(c4));
        assertFalse(sut.checkColour(c5));
        assertFalse(sut.checkColour(c6));
        assertFalse(sut.checkColour(c7));
        assertFalse(sut.checkColour(c8));
        assertFalse(sut.checkColour(c9));
        assertTrue(sut.checkColour(c10));
    }
}
