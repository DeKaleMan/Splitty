package commons;

import org.junit.jupiter.api.BeforeAll;

import java.util.Date;
import java.util.UUID;
public class ParticipantTest {

    // this test is for Participant and ParticipantId

    @BeforeAll
    public void setup() {
        Event e1 = new Event("Party"
                , new Date(2005, 10, 10)
                , ""
                , "The part is at .....");
        Event e2 = new Event("Swimming pool event"
                , new Date(2005, 10, 10)
                , ""
                , "The swimming pool is open from....");

        Participant p1 = new Participant("John", 0.0, "NLABNA2049250232522", "ABNANL02",
                "john.smith@gmail.com", "John Smith", UUID.randomUUID().toString(), e1);
        Participant p2 = new Participant("Kaitlin", 0.0, "AMCSK2225228939", "OKAMKL34",
                "kaitlin@gmail.com", "Kaitlin", UUID.randomUUID().toString(), e1);
        Participant p3 = new Participant("George", 0.0, "PLESC0252242761032", "PASKEE45",
                "george.smith@gmail.com", "George Smith", UUID.randomUUID().toString(), e2);
        e2.setHost(p3.getUuid());
        e1.setHost(p1.getUuid());
    }
}
