package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    @Test
    void testParticipantConstructorAndEquals() {
        String name = "Yavor";
        double balance = 12345.67;
        String iBan = "DE44500105175407324288"; // Example IBAN
        String bIC = "DEUTDEFFXXX"; // Example BIC
        String accountHolder = "Yavor";
        String email = "yavor@tudelft.nl";

        Participant participant = new Participant(name, balance, iBan, bIC, accountHolder, email);

        assertAll("Constructor assigns and retrieves values correctly",
                () -> assertEquals(name, participant.getName(), "Name mismatch"),
                () -> assertEquals(balance, participant.getBalance(), "Balance mismatch"),
                () -> assertEquals(iBan, participant.getIBan(), "IBAN mismatch"),
                () -> assertEquals(bIC, participant.getBIC(), "BIC mismatch"),
                () -> assertEquals(accountHolder, participant.getAccountHolder(), "Account holder mismatch"),
                () -> assertEquals(email, participant.getEmail(), "Email mismatch")
        );
    }

    @Test
    void testEquals() {
        Participant participant1 = new Participant("Yavor", 9876.54, "GB29NWBK60161331926819", "NWBKGB22XXX", "Yavor", "yavor@tudelft.nl");
        Participant participant2 = new Participant("Yavor", 9876.54, "GB29NWBK60161331926819", "NWBKGB22XXX", "Yavor", "yavor@tudelft.nl");
        Participant participant3 = new Participant("Jesse", 8765.43, "FR1420041010050500013M02606", "PSSTFRPPXXX", "Jesse", "jesse@tudelft.nl");

        assertAll("Testing equals",
                () -> assertEquals(participant1, participant2, "Participants with same email should be equal"),
                () -> assertNotEquals(participant1, participant3, "Participants with different emails should not be equal")
        );
    }

    @Test
    void testHashCode() {
        String email = "yavor@tudelft.nl";
        Participant participant = new Participant("Yavor", 9876.54, "GB29NWBK60161331926819", "NWBKGB22XXX", "Yavor", "yavor@tudelft.nl");

        assertEquals(email.hashCode(), participant.hashCode(), "Hash code should be based on email");
    }
}
