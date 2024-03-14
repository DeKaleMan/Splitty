//package server.api;
//
//import commons.Participant;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class ParticipantControllerTest {
//
//    private ParticipantController participantController;
//    private TestParticipantRepository testParticipantRepository;
//
//    @BeforeEach
//    void setUp() {
//        testParticipantRepository = new TestParticipantRepository();
//        participantController = new ParticipantController(testParticipantRepository, null);
//    }
//
//    @Test
//    void testGetAllParticipants() {
//        Participant participant1 = new Participant("Yavor", 100.0, "IBAN1", "BIC1", "Yavor", "yavor@tudelft.nl");
//        Participant participant2 = new Participant("Jesse", 200.0, "IBAN2", "BIC2", "Jesse", "Jesse@tudelft.nl");
//        testParticipantRepository.addParticipant(participant1);
//        testParticipantRepository.addParticipant(participant2);
//
//        List<Participant> participants = participantController.getAllParticipants();
//        assertEquals(2, participants.size());
//    }
//
//    @Test
//    void testGetParticipantByEmailFound() {
//        String email = "yavor@tudelft.nl";
//        Participant participant = new Participant("Yavor", 100.0, "IBAN1", "BIC1", "Yavor", email);
//        testParticipantRepository.addParticipant(participant);
//
//        ResponseEntity<Participant> response = participantController.getParticipantByEmail(email);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(participant, response.getBody());
//    }
//
//    @Test
//    void testGetParticipantByEmailNotFound() {
//        ResponseEntity<Participant> response = participantController.getParticipantByEmail("non.existent@tudelft.nl");
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//}
