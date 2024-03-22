package server.api;

import commons.Event;
import commons.Participant;
import commons.dto.ParticipantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParticipantControllerTest {

    private ParticipantController participantController;
    private TestParticipantRepository testParticipantRepository;
    private TestEventRepository testEventRepository;
    private Event event;
    @BeforeEach
    void setUp() {
        testParticipantRepository = new TestParticipantRepository();
        testEventRepository = new TestEventRepository();
        participantController = new ParticipantController(testParticipantRepository, testEventRepository);

        event = new Event("Event1", new Date(10, 10, 2005), "Yavor", "cool event");
        event.setId(1);
        testEventRepository.save(event);
    }

    @Test
    void testGetAllParticipants() {
        Participant participant1 = new Participant("Yavor", 100.0,
                "IBAN1", "BIC1", "yavor@tudelft.nl", "uuid", null);
        Participant participant2 = new Participant("Jesse", 200.0,
                "IBAN2", "BIC2", "jesse@tudelft.nl", "uuid2", null);

        testParticipantRepository.save(participant1);
        testParticipantRepository.save(participant2);

        List<Participant> participants = participantController.getAllParticipants();
        assertEquals(2, participants.size());
    }

    @Test
    void testGetParticipantNotFoundInvalidEvent() {
        ResponseEntity<Participant> response = participantController
                .getParticipantById("uuid", 2);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetParticipantNotFoundInvalidParticipant() {
        ResponseEntity<Participant> response = participantController
                .getParticipantById("notexisting", 1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateParticipant() {
        Participant participant = new Participant("Yavor", 100.0,
                "IBAN1", "BIC1", "yavor@tudelft.nl", "uuid", event);
        testParticipantRepository.save(participant);
        ResponseEntity<Participant> response = participantController.createParticipant(
                new ParticipantDTO(participant.getName(), participant.getBalance(),
                        participant.getIBan(), participant.getBIC(), participant.getEmail(),
                        event.getId(), participant.getUuid()));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateParticipantNotFoundInvalidEvent() {
        ResponseEntity<Participant> response = participantController
                .createParticipant(new ParticipantDTO("Yavor", 100.0,
                        "IBAN1", "BIC1", "yavor@tudelft.nl", 2, "uuid2"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetParticipantById() {

        Participant participant1 = new Participant("Yavor", 100.0,
                "IBAN1", "BIC1", "yavor@tudelft.nl", "uuid", event);
        testParticipantRepository.save(participant1);

        ResponseEntity<Participant> response = participantController
                .getParticipantById("uuid", event.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participant1, response.getBody());
    }

    @Test
    void testUpdateParticipant() {
        Participant participant = new Participant("Yavor", 100.0,
                "IBAN1", "BIC1", "yavor@tudelft.nl", "uuid", event);
        testParticipantRepository.save(participant);
        ParticipantDTO pdto = new ParticipantDTO("Ivan", 22,
                "iban2", "BIC2", "angel@tudelft.nl", event.getId(), "uuid1");
        ResponseEntity<Participant> response = participantController.updateParticipant("uuid", event.getId(), pdto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ivan", response.getBody().getName());
        assertEquals(22, response.getBody().getBalance());
        assertEquals("iban2", response.getBody().getIBan());
        assertEquals("BIC2", response.getBody().getBIC());
        assertEquals("angel@tudelft.nl", response.getBody().getEmail());
        assertEquals("uuid1", response.getBody().getUuid());
    }

}
