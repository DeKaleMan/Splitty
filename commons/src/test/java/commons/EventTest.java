package commons;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {
    String participant1 = "Stijn";
    String participant2 = "Yavor";
    String exp1 = "fries";
    String exp2 = "cola";
    String exp3 = "burger";

    List<String> participantList = List.of(participant2, participant1);

    List<String> expensesList = List.of(exp1,exp2,exp3);

    Event e1 = new Event("Take-away", "06/07/2004", participantList, "Lauren", expensesList, "We ordered food from the local BurgerKing");

    @Test
    public void toStringTest(){
        String res = "This is the Take-away event. That is summarized as: We ordered food from the local BurgerKing. It was created on 06/07/2004. The participants are: Yavor, Stijn. Lauren is the person that created the event. The expenses in the list are: fries, cola, burger.";
        assertEquals(res, e1.toString());
    }

}
